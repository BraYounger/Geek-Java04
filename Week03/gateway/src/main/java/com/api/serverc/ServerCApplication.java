package com.api.serverc;

import com.api.consts.CommonConsts;
import com.api.serverc.inbound.HttpServerC;

public class ServerCApplication {

    public static void main(String[] args) {
        System.out.println(CommonConsts.SERVER_C_NAME + " starting...");
        HttpServerC server = new HttpServerC();
        System.out.println(CommonConsts.SERVER_C_NAME + " started at " + CommonConsts.HOST + CommonConsts.SERVER_C_PORT);
        try {
            server.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
