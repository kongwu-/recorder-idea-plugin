package com.github.kongwu.recorder.plugin.agent.transport;

import com.github.kongwu.recorder.common.logger.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class AgentServer {

    private static final Logger logger = Logger.getLogger(AgentServer.class);

    private boolean running = false;

    private ServerSocket serverSocket;

    private SocketChannel transportChannel;

    public void start(int port){
        try {
            if(running){
                logger.info("Agent Server already in running state!");
                return;
            }
            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(port));
            running = true;
            AgentServerRunnable agentServerAcceptor = new AgentServerRunnable(serverSocket);
            new Thread(agentServerAcceptor).start();
            serverSocket.close();
        } catch (IOException e) {
            logger.error("server start failed!",e);
        }
    }

    public void close(){
        try {
            serverSocket.close();
        } catch (IOException e) {
            logger.info("server close failed, ignore..",e);
        }
    }

    public class AgentServerRunnable implements Runnable{

        private Logger logger = Logger.getLogger(AgentServerRunnable.class);

        private ServerSocket serverSocket;

        public AgentServerRunnable(ServerSocket serverSocket) {
            this.serverSocket = serverSocket;
        }

        private ByteBuffer lengthBuffer = ByteBuffer.allocate(Integer.BYTES);


        @Override
        public void run() {
            try {
                Socket socket = serverSocket.accept();
                SocketChannel socketChannel = socket.getChannel();

                while (lengthBuffer.hasRemaining()){
                    socketChannel.read(lengthBuffer);
                }
                lengthBuffer.flip();
                int length = lengthBuffer.getInt();
                lengthBuffer.reset();
                ByteBuffer requestBuffer = ByteBuffer.allocate(length);

                while (requestBuffer.hasRemaining()){
                    socketChannel.read(requestBuffer);
                }
                byte[] requestBodyBytes = requestBuffer.array();



            } catch (IOException e) {
                logger.error("server process failed!",e);
            }
        }
    }
}
