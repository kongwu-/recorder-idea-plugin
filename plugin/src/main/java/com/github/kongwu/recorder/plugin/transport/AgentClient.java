package com.github.kongwu.recorder.plugin.transport;

import com.github.kongwu.recorder.common.logger.Logger;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;

public class AgentClient {

    private Logger logger = Logger.getLogger(AgentClient.class);

    private EventLoopGroup eventLoopGroup;
    private Channel channel;

    public void connect(int port){
        eventLoopGroup = new NioEventLoopGroup(1);
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.handler(new LoggingHandler());

        try {
            Channel channel = bootstrap.connect("127.0.0.1", port).sync().channel();
        } catch (InterruptedException e) {
            logger.error("connect failed!",e);
        }
    }
}
