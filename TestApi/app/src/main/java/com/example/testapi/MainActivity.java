package com.example.testapi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    ListView lst;
    Context ctx;
    ArrayAdapter<note_info> adp;
    BottomNavigationView bnv;


    String http_get(String req) throws IOException
    {
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int idItem = item.getItemId();
        switch (idItem){
            case R.id.homeMenu:
                Toast.makeText(ctx, "id = " + item.getItemId(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.payMenu:
                Toast.makeText(ctx, "id = " + item.getItemId(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.historyMenu:
                Toast.makeText(ctx, "id = " + item.getItemId(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.dialogMenu:
                Toast.makeText(ctx, "id = " + item.getItemId(), Toast.LENGTH_SHORT).show();
                break;
        }

        return true;
    }

    class note_info {
        public  int id;
        public String title;

        public note_info(int i, String t){
            id = i;
            title = t;
        }

        public String toString(){
            return title;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bnv = (BottomNavigationView)findViewById(R.id.bnv);
        bnv.setOnNavigationItemSelectedListener(this);

        ctx = this;
        lst = (ListView)findViewById(R.id.myList);
        adp = new ArrayAdapter<note_info>(this, android.R.layout.simple_list_item_1);

        lst.setAdapter(adp);

        lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                note_info x = adp.getItem(position);
                Toast.makeText(ctx, String.valueOf(x.id) + " -- " + x.title, Toast.LENGTH_LONG).show();
            }
        });

        update_notes();
    }


    class mythread implements Runnable{

        public void run() {
            try {
                final String res = http_get("http://127.0.0.1:8080/api/add-note");
                JSONObject root = new JSONObject(res);

                boolean result = root.getBoolean("result");
                int id = root.getInt("id");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        update_notes();
                        Toast.makeText(ctx, res, Toast.LENGTH_LONG).show();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    class mythread2 implements  Runnable{


        public void run() {
            try {
                final String res = http_get("http://localhost:8080/api/get-note-list");
                JSONObject root = new JSONObject(res);
                boolean result = root.getBoolean("result");
                final JSONArray list = root.getJSONArray("list");

                runOnUiThread(new Runnable() {

                    public void run() {
                        adp.clear();
                        for(int i = 0; i < list.length(); i++){
                            try {
                                JSONObject item = list.getJSONObject(i);
                                int id = item.getInt("id");
                                String title = item.getString("title");
                                adp.add(new note_info(id, title));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        adp.notifyDataSetInvalidated();
                        Toast.makeText(ctx, res, Toast.LENGTH_LONG).show();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void on_add_click(View v){
        mythread my = new mythread();
        Thread th = new Thread(my);
        th.start();
    }

    void update_notes(){
        mythread2 my = new mythread2();
        Thread th = new Thread(my);
        th.start();
    }
}
