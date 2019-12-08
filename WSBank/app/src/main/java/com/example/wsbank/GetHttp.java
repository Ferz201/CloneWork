package com.example.wsbank;

import android.content.SharedPreferences;

import java.io.BufferedInputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.content.Context.MODE_PRIVATE;

public class GetHttp {

    public String http_post(String req, String postRequest) throws Exception{
        URL url = new URL(req);
        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoInput(true);
        con.setDoOutput(true);
        con.setRequestMethod("POST");

        con.getOutputStream().write(postRequest.getBytes("utf-8"));

        BufferedInputStream inp = new BufferedInputStream(con.getInputStream());

        byte[] buf = new byte[512];
        String res = "";

        while (true){
            int num = inp.read(buf);
            if(num < 0) break;

            res += new String(buf, 0, num);
        }


        inp.close();
        con.disconnect();
        return res;
    }


    public String http_delete(String req, String delRequest) throws Exception {
        URL url = new URL(req);
        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);
        con.setDoInput(true);
        con.setRequestMethod("DELETE");

        con.getOutputStream().write(delRequest.getBytes("utf-8"));

        BufferedInputStream inp = new BufferedInputStream(con.getInputStream());

        byte[] buf = new byte[512];
        String res = "";

        while (true){
            int num = inp.read(buf);
            if(num < 0) break;

            res += new String(buf, 0, num);
        }

        inp.close();
        con.disconnect();
        return res;
    }


    public String http_put(String req, String putRequest) throws Exception {
        URL url = new URL(req);
        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setRequestMethod("PUT");
        con.setDoInput(true);
        con.setDoOutput(true);
        con.setRequestProperty("Content-Type", "application/json");

        con.getOutputStream().write(putRequest.getBytes("utf-8"));

        BufferedInputStream inp = new BufferedInputStream(con.getInputStream());

        byte[] buf = new byte[512];
        String res = "";

        while (true){
            int num = inp.read(buf);
            if(num < 0) break;

            res += new String(buf, 0, num);
        }

        inp.close();
        con.disconnect();
        return res;
    }


    public Boolean getUser(SharedPreferences sp){
        if(sp.contains("token")){
//            String str = sp.getString("token", "");
            return true;
        }
        return false;
    }
}
