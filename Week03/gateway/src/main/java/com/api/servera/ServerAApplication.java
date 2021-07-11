package com.api.servera;

import com.api.consts.CommonConsts;
import com.api.servera.inbound.HttpServerA;

public class ServerAApplication {

    public static void main(String[] args) {
        System.out.println(CommonConsts.SERVER_A_NAME + " starting...");
        HttpServerA server = new HttpServerA();
        System.out.println(CommonConsts.SERVER_A_NAME + " started at " + CommonConsts.HOST + CommonConsts.SERVER_A_PORT);
        try {
            server.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
