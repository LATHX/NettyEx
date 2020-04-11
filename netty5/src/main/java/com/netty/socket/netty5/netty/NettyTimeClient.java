package com.netty.socket.netty5.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class NettyTimeClient {
    public void connect(int port, String host) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true).handler(new ChannelInitializer<SocketChannel>() {

                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {

                    // 以 $_ 作为消息分隔符 消息长度达到 1024 仍没有出现分隔符，则抛出异常
                    ByteBuf delimiter = Unpooled.copiedBuffer("$_".getBytes());
                    socketChannel.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, delimiter));

                    // 判断 \n 或 \r\n 为结束位置
//                    socketChannel.pipeline().addLast(new LineBasedFrameDecoder(1024));
                    socketChannel.pipeline().addLast(new StringDecoder());
                    socketChannel.pipeline().addLast(new NettyTimeClientHandler());
                }
            });
            ChannelFuture f = b.connect(host, port).sync();
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        new NettyTimeClient().connect(8080, "127.0.0.1");
    }
}
