package com.api.serverc.inbound;

import com.api.serverc.outbound.HttpServerCOutboundHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.util.ReferenceCountUtil;
import lombok.Data;

import java.util.Iterator;
import java.util.Map;

@Data
public class HttpServerCInboundHandler extends ChannelInboundHandlerAdapter {

    private HttpServerCOutboundHandler outboundHandler = new HttpServerCOutboundHandler();

    @Override
    public void channelReadComplete(ChannelHandlerContext context) {
        context.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext context, Object msg) {
        try {
            FullHttpRequest request = (FullHttpRequest) msg;
            printHeaders(request.headers());
            outboundHandler.handle(context, request);
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    private void printHeaders(HttpHeaders headers) {
        Iterator<Map.Entry<String, String>> iterator = headers.iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}
