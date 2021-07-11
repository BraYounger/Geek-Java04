package com.api.gateway.filter.pre;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

import java.util.List;

public class HttpPreFilter {

    public void doFilter (ChannelHandlerContext context, FullHttpRequest request, List<String> proxyServers) {
        request.headers().add("extra-pre-attr", "hello");
    }


}
