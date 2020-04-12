package com.netty.socket.netty4.push;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

public class WebSocketServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    private MsgProcessor processor = new MsgProcessor();

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {
        processor.sendMsg(channelHandlerContext.channel(), textWebSocketFrame.text());
    }
}
