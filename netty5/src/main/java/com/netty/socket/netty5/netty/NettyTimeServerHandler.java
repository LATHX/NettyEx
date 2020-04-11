package com.netty.socket.netty5.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;


public class NettyTimeServerHandler extends ChannelHandlerAdapter {
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String body = (String) msg;
        String currentTime = "";
        if ("QUERY TIME ORDER".equalsIgnoreCase(body)) {
            currentTime = new java.util.Date(System.currentTimeMillis()).toString();
        } else {
            currentTime = "BAD ORDER";
        }
        // LineBasedFrameDecoder 用 \r\n
//        currentTime += System.getProperty("line.separator");

        // DelimiterBasedFrameDecoder 自定义 $_ 消息分隔符
        currentTime += "$_";
        ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
        ctx.writeAndFlush(resp);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
}
