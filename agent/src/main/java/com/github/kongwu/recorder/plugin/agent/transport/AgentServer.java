package com.github.kongwu.recorder.plugin.agent.transport;

import com.github.kongwu.recorder.common.logger.Logger;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LoggingHandler;

public class AgentServer {

    private static final Logger logger = Logger.getLogger(AgentServer.class);

    private volatile boolean started = false;

    private Channel acceptChannel;

    private NioEventLoopGroup serverEventLoopGroup;

    public void start(int port) {
        try {
            if (started) {
                logger.info("Agent Server already in running state!");
                return;
            }

            serverEventLoopGroup = new NioEventLoopGroup(1);
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.group(serverEventLoopGroup);
            serverBootstrap.childHandler(new ChannelInitializer() {
                @Override
                protected void initChannel(Channel ch) throws Exception {
                    ch.pipeline().addFirst(new LoggingHandler());
                    ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,Integer.BYTES,0,Integer.BYTES));
                    ch.pipeline().addLast(new RequestDecoder());
                    ch.pipeline().addLast(new ResponseEncoder());
                    ch.pipeline().addLast(new AgentServerHandler(AgentServer.this));
                }
            });
            acceptChannel = serverBootstrap.bind(port).sync().channel();
            started = true;
        } catch (InterruptedException e) {
            logger.error("server start failed!", e);
        }
    }

    public void close() {
        started = false;
        serverEventLoopGroup.shutdownGracefully().syncUninterruptibly();
        acceptChannel.close();
    }

}
