package com.netty.socket.netty5.aio;

public class AioTimeServer {
    public static void main(String[] args) {
        AsyncTimeServerHandler timeServerHandler = new AsyncTimeServerHandler(8080);
        new Thread(timeServerHandler, "AIO-AsyncTimeServerHandler").start();
    }
}
