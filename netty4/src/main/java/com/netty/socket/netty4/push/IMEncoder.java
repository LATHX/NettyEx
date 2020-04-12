package com.netty.socket.netty4.push;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.msgpack.MessagePack;

public class IMEncoder extends MessageToByteEncoder<IMMessage> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, IMMessage imMessage, ByteBuf byteBuf) throws Exception {
        byteBuf.writeBytes(new MessagePack().write(imMessage));
    }
    public String encode(IMMessage msg){
        if(null == msg){ return ""; }
        String prex = "[" + msg.getCmd() + "]" + "[" + msg.getTime() + "]";
        if(IMP.LOGIN.getName().equals(msg.getCmd()) ||
                IMP.FLOWER.getName().equals(msg.getCmd())){
            prex += ("[" + msg.getSender() + "][" + msg.getTerminal() + "]");
        }else if(IMP.CHAT.getName().equals(msg.getCmd())){
            prex += ("[" + msg.getSender() + "]");
        }else if(IMP.SYSTEM.getName().equals(msg.getCmd())){
            prex += ("[" + msg.getOnline() + "]");
        }
        if(!(null == msg.getContent() || "".equals(msg.getContent()))){
            prex += (" - " + msg.getContent());
        }
        return prex;
    }
}
