package com.github.kongwu.recorder.plugin.transport;

import com.github.kongwu.recorder.common.logger.Logger;
import com.github.kongwu.recorder.common.transport.PacketDecoder;
import com.github.kongwu.recorder.common.transport.PacketEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;

public class AgentClient {

    private Logger logger = Logger.getLogger(AgentClient.class);

    private EventLoopGroup eventLoopGroup;

    private AgentTunnel agentTunnel;

    public AgentClient(AgentTunnel agentTunnel) {
        this.agentTunnel = agentTunnel;
    }

    public Channel connect(int port){
        eventLoopGroup = new NioEventLoopGroup(1);
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.handler(new ChannelInitializer() {
            @Override
            protected void initChannel(Channel ch) throws Exception {
                ch.pipeline().addFirst(new LoggingHandler());

                ch.pipeline().addLast(new PacketEncoder());
                ch.pipeline().addLast(new PacketDecoder());
                ch.pipeline().addLast(new AgentClientHandler(agentTunnel));
            }
        });

        try {
            Channel channel = bootstrap.connect("127.0.0.1", port).sync().channel();
            return channel;
        } catch (InterruptedException e) {
            logger.error("connect failed!",e);
        }
        return null;
    }

    public void close(){
        eventLoopGroup.shutdownGracefully().syncUninterruptibly();
    }
}
