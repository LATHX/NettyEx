package com.netty.socket.netty5.msgpack;

import org.msgpack.MessagePack;
import org.msgpack.template.Templates;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MsgPack {
    public static void main(String[] args) throws IOException {
        List<String> src = new ArrayList<>();
        src.add("1");
        src.add("2");
        src.add("3");
        MessagePack msgpack = new MessagePack();
        byte[] write = msgpack.write(src);
        List<String> dst = msgpack.read(write, Templates.tList(Templates.TString));
        System.out.println(dst.get(0));
        System.out.println(dst.get(1));
        System.out.println(dst.get(2));
    }
}
