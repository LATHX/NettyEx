package com.netty.socket.netty4.customerRPC.proxy;

import com.netty.socket.netty4.customerRPC.api.IRpcService;

public class ProxyMain {
    public static void main(String[] args) {
        IRpcService service = RpcProxy.create(IRpcService.class);
        System.out.println(service.add(8, 2));
    }
}
