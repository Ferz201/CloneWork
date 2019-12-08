package com.example.wsbank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONObject;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    GetHttp getHttp = new GetHttp();
    SharedPreferences sp;
    String NAME_TABL_SHAREDPREFERENCES = "WSBank";
    String NAME_TOKEN = "token";
    String FIRSTNAME = "firstname";
    String MIDDLENAME = "middlename";
    Context ctx;

    LinearLayout btnBanks, btnCurrency;
    Button btnLogin;
    AlertDialog alertDialog;
    View mView;
    String token;

    Fragment fragmentHome;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = this;
        postToken();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnBanks:
                Toast.makeText(this, "Banks loading...", Toast.LENGTH_SHORT).show();
                break;

            case R.id.btnCurrency:
                Intent iCurrency = new Intent(this, CurrencyActivity.class);
                startActivity(iCurrency);
                break;

            case R.id.btnLogin:
                dialLoginShow();
                break;

            case R.id.btnLog:
                EditText etLogin = mView.findViewById(R.id.etLogin);
                String login = String.valueOf(etLogin.getText());
                EditText etPassword = mView.findViewById(R.id.etPassword);
                String password = String.valueOf(etPassword.getText());
                postReg(login, password);
                alertDialog.dismiss();
                break;

            case R.id.btnCencel:
                alertDialog.dismiss();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int idClick = item.getItemId();
        if(idClick == R.id.profileMenu){
            Fragment fragment = new FragmentProfile();
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment, fragment).addToBackStack(null).commit();
        } else if (idClick == android.R.id.home) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }



    public void postReg(final String login, final String password){

        new Thread(new Runnable() {


            @Override
            public void run() {
                try {
                    String url = "http://awh2.ddns.net:16482/login";
                    String postRequest = "{ \"username\":\"" + login + "\", \"password\":\"" + password +  "\"}";
                    String res = getHttp.http_post(url, postRequest);
                    JSONObject root = new JSONObject(res);
                    token = root.getString("token");
                    sp = getSharedPreferences(NAME_TABL_SHAREDPREFERENCES, MODE_PRIVATE);
                    SharedPreferences.Editor ed = sp.edit();
                    ed.putString(NAME_TOKEN, token);
                    ed.commit();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            regUser();
                        }
                    });

                } catch (Exception e){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ctx, "Неверный логин или пароль", Toast.LENGTH_LONG).show();
                        }
                    });
                }

            }
        }).start();
    }



    public void postToken(){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        sp = getSharedPreferences(NAME_TABL_SHAREDPREFERENCES, MODE_PRIVATE);
                        String strToken = sp.getString(NAME_TOKEN, "");
                        String url = "http://awh2.ddns.net:16482/getuser";
                        String postRequest = "{ \"token\": \"" + strToken + "\" }";
                        final String res = getHttp.http_post(url, postRequest);

                        JSONObject root = new JSONObject(res);
                        final String firstname = root.getString("firstname");
                        final String middlename = root.getString("middlename");

                        SharedPreferences.Editor ed = sp.edit();
                        ed.putString(FIRSTNAME, firstname);
                        ed.putString(MIDDLENAME, middlename);
                        ed.commit();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                homeActivity(firstname, middlename);
                            }
                        });



                    } catch (Exception e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                regActivity();
                            }
                        });
                    }
                }
            }).start();
    }


    public void homeActivity(String firstname, String middlename){
        getSupportActionBar().show();
        getSupportActionBar().setTitle(firstname + " " + middlename);
        setContentView(R.layout.activity_home);

        sp = getSharedPreferences(NAME_TABL_SHAREDPREFERENCES, MODE_PRIVATE);
        String token = sp.getString(NAME_TOKEN, "");
        fragmentHome = new FragmentHome(token);
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment, fragmentHome);
        fragmentTransaction.commit();
    }


    public void regActivity(){
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        btnBanks = findViewById(R.id.btnBanks);
        btnBanks.setOnClickListener(this);
        btnCurrency = findViewById(R.id.btnCurrency);
        btnCurrency.setOnClickListener(this);
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
    }

    public void dialLoginShow()
    {
        AlertDialog.Builder adBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        mView = inflater.inflate(R.layout.dialog_login, null);
        adBuilder.setView(mView);
        alertDialog = adBuilder.create();
        alertDialog.show();

        Button btnLog, btnCencel;
        btnCencel = mView.findViewById(R.id.btnCencel);
        btnCencel.setOnClickListener(this);
        btnLog = mView.findViewById(R.id.btnLog);
        btnLog.setOnClickListener(this);
    }

    public void regUser(){
        sp = getSharedPreferences(NAME_TABL_SHAREDPREFERENCES, MODE_PRIVATE);
        Boolean token = getHttp.getUser(sp);
        if(token){
            postToken();
        }
    }
}