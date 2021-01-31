package com.github.kongwu.recorder.plugin.agent;

import com.alibaba.bytekit.utils.AgentUtils;
import com.github.kongwu.recorder.common.constant.Constants;
import com.github.kongwu.recorder.common.model.PacketConstant;
import com.github.kongwu.recorder.common.model.RequestPacket;
import com.github.kongwu.recorder.common.model.ResponsePacket;
import com.github.kongwu.recorder.common.transport.PacketDecoder;
import com.github.kongwu.recorder.common.transport.PacketEncoder;
import com.github.kongwu.recorder.plugin.agent.enhance.AgentHolder;
import com.github.kongwu.recorder.plugin.agent.transport.AgentServer;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
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
        bootstrap.handler(new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel ch) throws Exception {
                ch.pipeline().addFirst(new LoggingHandler());
                ch.pipeline().addLast(new PacketEncoder());
                ch.pipeline().addLast(new PacketDecoder());
                ch.pipeline().addLast(new SimpleChannelInboundHandler<Object>() {
                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
                        if(msg instanceof RequestPacket){
                            System.out.println(((RequestPacket) msg).getBody());
                        }
                        if(msg instanceof ResponsePacket){
                            System.out.println("received msg");
                        }
                    }
                });
            }
        });

        Channel channel = bootstrap.connect("127.0.0.1", PORT).sync().channel();

        ByteBuf byteBuf = channel.alloc().buffer();
        channel.writeAndFlush(new RequestPacket(PacketConstant.EVENT_TRACE,"com.github.kongwu.recorder.plugin.agent.Sample"));
    }
}
