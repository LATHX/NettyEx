package com.netty.socket.netty5.aio;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class AcceptCompletionHandler implements CompletionHandler {
    @Override
    public void completed(Object result, Object attachment) {
        AsynchronousSocketChannel asynchronousSocketChannel = (AsynchronousSocketChannel) result;
        AsyncTimeServerHandler asyncTimeServerHandler = (AsyncTimeServerHandler) attachment;
        asyncTimeServerHandler.asynchronousServerSocketChannel.accept(attachment, this);
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        asynchronousSocketChannel.read(buffer, buffer, new ReadCompletionHandler(asynchronousSocketChannel));
    }

    @Override
    public void failed(Throwable exc, Object attachment) {
        exc.printStackTrace();
        AsyncTimeServerHandler asyncTimeServerHandler = (AsyncTimeServerHandler) attachment;
        asyncTimeServerHandler.latch.countDown();
    }
}
