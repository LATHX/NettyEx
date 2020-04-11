package com.netty.socket.netty5.msgpack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.msgpack.MessagePack;

import java.util.List;

public class MsgDecode extends MessageToMessageDecoder<ByteBuf> {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        int length = byteBuf.readableBytes();
        byte[] array = new byte[length];
        byteBuf.getBytes(byteBuf.readerIndex(), array, 0, length);
        MessagePack messagePack = new MessagePack();
        list.add(messagePack.read(array));
    }
}
