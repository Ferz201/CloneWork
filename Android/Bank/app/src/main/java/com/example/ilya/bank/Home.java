package com.example.ilya.bank;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class Home extends Activity implements View.OnClickListener, View.OnTouchListener {

    LinearLayout btnHome, btnPay, btnHistory, btnDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        // Получаем данные о пользователе
        Intent intent = getIntent();
        Account account = (Account)intent.getSerializableExtra("account");

        // ActionBar
//        ActionBar actionBar = getActionBar();
//        actionBar.setDisplayShowHomeEnabled(false);
//        actionBar.setDisplayShowTitleEnabled(false);
//        actionBar.setDisplayShowCustomEnabled(true);
//        actionBar.setCustomView(R.layout.action_bar_home);
//        TextView tvTitle = (TextView)findViewById(R.id.tvTitle);
//        tvTitle.setText(account.getName() + " " + account.getPatronymic());
//        SearchView svFind = (SearchView)findViewById(R.id.svFind);
//        ImageView ivUser = (ImageView)findViewById(R.id.ivUser);




        //Menu
        btnHome = (LinearLayout)findViewById(R.id.llHome);
        btnHome.setOnClickListener(this);
        btnHome.setOnTouchListener(this);
        btnPay = (LinearLayout)findViewById(R.id.llPay);
        btnPay.setOnClickListener(this);
        btnPay.setOnTouchListener(this);
        btnHistory = (LinearLayout)findViewById(R.id.llHistory);
        btnHistory.setOnClickListener(this);
        btnHistory.setOnTouchListener(this);
        btnDialog = (LinearLayout)findViewById(R.id.llDialog);
        btnDialog.setOnClickListener(this);
        btnDialog.setOnTouchListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.llHome:
                Toast.makeText(this, "btn Home", Toast.LENGTH_SHORT).show();
                break;
            case R.id.llPay:
                Toast.makeText(this, "btn Pay", Toast.LENGTH_SHORT).show();
                break;
            case R.id.llHistory:
                Toast.makeText(this, "btn History", Toast.LENGTH_SHORT).show();
                break;
            case R.id.llDialog:
                Toast.makeText(this, "btn Dialog", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_UP) {
            v.setBackgroundColor(Color.parseColor("#242424"));
        } else if(event.getAction() == MotionEvent.ACTION_DOWN) {
            v.setBackgroundColor(Color.parseColor("#FF585858"));
        }

        return false;
    }
}
