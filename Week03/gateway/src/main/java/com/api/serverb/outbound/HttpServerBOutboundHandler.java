package com.api.serverb.outbound;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpUtil;

import static io.netty.handler.codec.http.HttpResponseStatus.NO_CONTENT;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class HttpServerBOutboundHandler {

    public void handle(ChannelHandlerContext context, FullHttpRequest request) {
        FullHttpResponse fullHttpResponse = null;
        try {
            fullHttpResponse = handleResponse();
        } catch (Exception e) {
            e.printStackTrace();
            fullHttpResponse = new DefaultFullHttpResponse(HTTP_1_1, NO_CONTENT);
            context.close();
        } finally {
            if (request != null) {
                if (!HttpUtil.isKeepAlive(request)) {
                    context.write(fullHttpResponse).addListener(ChannelFutureListener.CLOSE);
                } else {
                    context.write(fullHttpResponse);
                }
            }
            context.flush();
        }
    }

    private FullHttpResponse handleResponse() {
        byte[] body = "This is server B".getBytes();
        FullHttpResponse fullHttpResponse = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(body));
        fullHttpResponse.headers().add("Content-Type", "text/html;charset=utf-8");
        fullHttpResponse.headers().add("Content-Length", body.length);
        return fullHttpResponse;
    }

}
