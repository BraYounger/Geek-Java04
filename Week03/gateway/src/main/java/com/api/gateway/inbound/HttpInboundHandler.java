package com.api.gateway.inbound;

import com.api.gateway.filter.pre.HttpPreFilter;
import com.api.gateway.outbound.HttpOutboundHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.ReferenceCountUtil;
import lombok.Data;

import java.util.List;

@Data
public class HttpInboundHandler extends ChannelInboundHandlerAdapter {

    private final List<String> proxyServers;
    private HttpPreFilter preFilter = new HttpPreFilter();
    private HttpOutboundHandler outboundHandler = new HttpOutboundHandler();

    public HttpInboundHandler(List<String> proxyServers) {
        this.proxyServers = proxyServers;
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext context) {
        context.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext context, Object msg) {
        try {
            FullHttpRequest request = (FullHttpRequest) msg;
            preFilter.doFilter(context, request, proxyServers);
            outboundHandler.handle(context, request, proxyServers);
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }
}
