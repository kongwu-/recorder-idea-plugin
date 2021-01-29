package com.github.kongwu.recorder.plugin.agent;

import com.alibaba.bytekit.utils.AgentUtils;
import com.github.kongwu.recorder.common.constant.Constants;
import com.github.kongwu.recorder.common.model.PacketType;
import com.github.kongwu.recorder.plugin.agent.enhance.AgentHolder;
import com.github.kongwu.recorder.plugin.agent.transport.AgentServer;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;

import java.io.UnsupportedEncodingException;
import java.lang.instrument.Instrumentation;

public class AgentTest {

    private static final int PORT = 10086;

    public static void main(String[] args) throws InterruptedException, UnsupportedEncodingException {
        Instrumentation instrumentation = AgentUtils.install();
        AgentHolder.setInstrumentation(instrumentation);

        AgentServer agentServer = new AgentServer();
        agentServer.start(PORT);
        Sample sample = new Sample();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    System.out.println(sample.randomStr(i));
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        sendTracePacket();
    }

    private static void sendTracePacket() throws InterruptedException, UnsupportedEncodingException {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup(1);
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.handler(new LoggingHandler());

        Channel channel = bootstrap.connect("127.0.0.1", PORT).sync().channel();

        ByteBuf byteBuf = channel.alloc().buffer();
        byteBuf.writeByte(PacketType.REQUEST_TRACE);
        byteBuf.writeBytes("com.github.kongwu.recorder.plugin.agent.Sample".getBytes(Constants.ENCODING));
        channel.writeAndFlush(byteBuf);
    }
}
