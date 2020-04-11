package com.netty.socket.netty5.nio;

public class NioTimeServer {
    public static void main(String[] args) {
        int port = 8080;
        MultiplexerTimeServer timeServer = new MultiplexerTimeServer(port);
        new Thread(timeServer, "NIO-MultiplexerTimerServer-001").start();
    }
}
