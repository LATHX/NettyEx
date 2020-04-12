package com.netty.socket.netty4.push;

import io.netty.channel.*;
import io.netty.handler.codec.http.FullHttpRequest;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.net.URL;

import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.LastHttpContent;

public class HttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private URL baseURL = HttpServerHandler.class.getResource("");
    private final String resources = "resources";

    private File getResource(String fileName) throws URISyntaxException {
        String basePath = baseURL.toURI().toString();
        int start = basePath.indexOf("class/");
        basePath = (basePath.substring(0, start) + "/classes/").replaceAll("/+", "/");
        String path = basePath + resources + "/" + fileName;
        path = !path.contains("file:") ? path : path.substring(5);
        path = path.replaceAll("//", "/");
        return new File(path);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
        String uri = fullHttpRequest.getUri();
        RandomAccessFile file = null;
        try {
            String page = uri.equals("/") ? "chat.html" : uri;
            file = new RandomAccessFile(getResource(page), "r");
        } catch (Exception e) {
            channelHandlerContext.fireChannelRead(fullHttpRequest.retain());
            return;
        }
        HttpResponse response = new DefaultHttpResponse(fullHttpRequest.getProtocolVersion(), HttpResponseStatus.OK);
        String contextType = "text/html;";
        if (uri.endsWith(".css")) {
            contextType = "text/css;";
        } else if (uri.endsWith(".js")) {
            contextType = "text/javascript;";
        } else if (uri.toLowerCase().matches(".*\\.(jpg|png|gif)$")) {
            String ext = uri.substring(uri.lastIndexOf("."));
            contextType = "image/" + ext;
        }
        response.headers().set(HttpHeaders.Names.CONTENT_TYPE, contextType + "charset=utf-8;");

        boolean keepAlive = HttpHeaders.isKeepAlive(fullHttpRequest);

        if (keepAlive) {
            response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, file.length());
            response.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        }
        channelHandlerContext.write(response);

        channelHandlerContext.write(new DefaultFileRegion(file.getChannel(), 0, file.length()));

        ChannelFuture future = channelHandlerContext.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
        if (!keepAlive) {
            future.addListener(ChannelFutureListener.CLOSE);
        }

        file.close();
    }
}
