package com.netty.socket.netty5.nio;


public class NioTimeClient {
    public static void main(String[] args) {
        new Thread(new TimeClientHandler("localhost", 8080), "TimeClient-01").start();
    }
}
