package com.example.notes;

import android.app.Activity;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiHelper {
    Activity ctx;

    public ApiHelper (Activity ctx){
        this.ctx = ctx;
    }

    public void on_ready(String res){}

    public void on_fail(){}

    String http_get(String req) throws IOException {
        URL url = new URL(req);
        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        BufferedInputStream inp = new BufferedInputStream(con.getInputStream());

        byte[] buf = new byte[512];
        String res = "";

        while (true){
            int num = inp.read(buf);
            if(num < 0) break;

            res += new String(buf, 0, num);
        }

        con.disconnect();
        return res;
    }


    public class NetOp implements Runnable{

        public String req;

        @Override
        public void run() {
            try {
                final String res = http_get(req);

                ctx.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        on_ready(res);
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void send(String req){
        NetOp nop = new NetOp();
        nop.req = req;

        Thread th = new Thread(nop);
        th.start();
    }
}
