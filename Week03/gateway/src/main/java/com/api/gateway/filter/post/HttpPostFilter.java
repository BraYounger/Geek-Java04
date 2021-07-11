package com.api.gateway.filter.post;

import io.netty.handler.codec.http.FullHttpResponse;

public class HttpPostFilter {

    public void doFilter(FullHttpResponse fullHttpResponse) {
        fullHttpResponse.headers().add("extra-post-attr", "hello");
    }
}
