package com.api.gateway.outbound;

import com.api.gateway.filter.post.HttpPostFilter;
import com.api.gateway.router.HttpEndpointRouter;
import com.api.gateway.router.RandomHttpEndpointRouter;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpUtil;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static io.netty.handler.codec.http.HttpResponseStatus.NO_CONTENT;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class HttpOutboundHandler {

    private HttpEndpointRouter router = new RandomHttpEndpointRouter();
    private HttpPostFilter postFilter = new HttpPostFilter();

    public void handle(ChannelHandlerContext context, FullHttpRequest request, List<String> proxyServers) {
        String newUrl = router.route(proxyServers) + request.uri();
        HttpResponse response = null;
        FullHttpResponse fullHttpResponse = null;
        try {
            response = sendRequest(newUrl, request);
            fullHttpResponse = handleResponse(response);
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

    private FullHttpResponse handleResponse(final HttpResponse response) throws IOException {
        byte[] body = EntityUtils.toByteArray(response.getEntity());
        FullHttpResponse fullHttpResponse = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(body));
        for (Header header : response.getAllHeaders()) {
            fullHttpResponse.headers().add(header.getName(), header.getValue());
        }
        postFilter.doFilter(fullHttpResponse);
        return fullHttpResponse;
    }

    private HttpResponse sendRequest(String url, FullHttpRequest fullHttpRequest) throws IOException {
        HttpClient client = new DefaultHttpClient();
        HttpUriRequest request = new HttpGet(url);
        Iterator<Map.Entry<String, String>> iterator = fullHttpRequest.headers().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            request.addHeader(entry.getKey(), entry.getValue());
        }
        return client.execute(request);
    }

}
