package com.netty.socket.aio;

public class AioTimeClient {
    public static void main(String[] args) {
        new Thread(new AsyncTimeClientHandler("localhost",8080),"AIO-client").start();
    }
}
