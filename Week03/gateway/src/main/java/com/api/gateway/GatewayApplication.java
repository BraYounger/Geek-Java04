package com.api.gateway;

import com.api.consts.CommonConsts;
import com.api.gateway.inbound.HttpInboundServer;

import java.util.Arrays;

public class GatewayApplication {

    public static void main(String[] args) {
        System.out.println(CommonConsts.GATEWAY_NAME + " starting...");
        HttpInboundServer server = new HttpInboundServer(CommonConsts.GATEWAY_PORT, Arrays.asList(CommonConsts.HOST + CommonConsts.SERVER_A_PORT,
                CommonConsts.HOST + CommonConsts.SERVER_B_PORT, CommonConsts.HOST + CommonConsts.SERVER_C_PORT));
        System.out.println(CommonConsts.GATEWAY_NAME + " started at " + CommonConsts.HOST + CommonConsts.GATEWAY_PORT);
        try {
            server.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
