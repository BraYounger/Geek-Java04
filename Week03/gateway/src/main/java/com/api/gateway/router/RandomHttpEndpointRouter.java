package com.api.gateway.router;

import java.util.List;
import java.util.Random;

public class RandomHttpEndpointRouter implements HttpEndpointRouter {

    @Override
    public String route(List<String> proxyServers) {
        int size = proxyServers.size();
        Random random = new Random(System.currentTimeMillis());
        return proxyServers.get(random.nextInt(size));
    }
}
