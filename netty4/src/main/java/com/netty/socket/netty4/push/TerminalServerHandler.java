package com.netty.socket.netty4.push;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class TerminalServerHandler extends SimpleChannelInboundHandler<IMMessage> {
    private MsgProcessor processor = new MsgProcessor();

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, IMMessage imMessage) throws Exception {
        processor.sendMsg(channelHandlerContext.channel(), imMessage);
    }
}
