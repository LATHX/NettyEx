package com.netty.socket.netty4.customerRPC.provider;

import com.netty.socket.netty4.customerRPC.api.IRpcHelloService;

public class RpcHelloServiceImpl implements IRpcHelloService {
    @Override
    public String hello(String name) {
        return "Hello" + name + "!";
    }
}
