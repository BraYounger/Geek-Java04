package com.api.serverb;

import com.api.consts.CommonConsts;
import com.api.serverb.inbound.HttpServerB;

public class ServerBApplication {

    public static void main(String[] args) {
        System.out.println(CommonConsts.SERVER_B_NAME + " starting...");
        HttpServerB server = new HttpServerB();
        System.out.println(CommonConsts.SERVER_B_NAME + " started at " + CommonConsts.HOST + CommonConsts.SERVER_B_PORT);
        try {
            server.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
