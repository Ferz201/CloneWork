package com.example.ilya.bank;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;


public class StartScreen extends Activity implements View.OnClickListener{

    LinearLayout btnOtdBanks, btnExchangeRates;
    Button btnEntry;
    TextView tvDate;

    private EditText etLogin, etPassword;
    public String log, pass;

    DBHelper dbHelper;
    SQLiteDatabase database;
    ContentValues contentValues;

    Toast toastLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();
        contentValues = new ContentValues();

        //setUser();

        Cursor cursor = database.query(DBHelper.TABLE_NAME, null, null, null, null, null, null);
        if(cursor.moveToFirst()){
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
            int login = cursor.getColumnIndex(DBHelper.KEY_LOGIN);
            int password = cursor.getColumnIndex(DBHelper.KEY_PASSWORD);
            int name = cursor.getColumnIndex(DBHelper.KEY_NAME);
            int surname = cursor.getColumnIndex(DBHelper.KEY_SURNAME);
            int patronymic = cursor.getColumnIndex(DBHelper.KEY_PATRONYMIC);
            do{
                Log.d("myLog", cursor.getInt(idIndex) + ", "
                        + cursor.getString(login) + ", "
                        + cursor.getString(password) + ", "
                        + cursor.getString(name) + ", "
                        + cursor.getString(surname) + ", "
                        + cursor.getString(patronymic));
            } while (cursor.moveToNext());
        }
        else {
            Log.d("myLog", "0 rows");
        }
        cursor.close();
        dbHelper.close();



        //Дата берётся с календаря
        Calendar date = Calendar.getInstance();
        tvDate = (TextView)findViewById(R.id.tvDate);
        tvDate.setText(date.get(Calendar.DAY_OF_MONTH) + "." + date.get(Calendar.MONTH) + "." + date.get(Calendar.YEAR));


        //Кнопки
        btnOtdBanks = (LinearLayout)findViewById(R.id.LLOtdBanks);
        btnOtdBanks.setOnClickListener(this);
        btnExchangeRates = (LinearLayout)findViewById(R.id.LLexchangeRates);
        btnExchangeRates.setOnClickListener(this);
        btnEntry = (Button)findViewById(R.id.btnEntry);
        btnEntry.setOnClickListener(this);


        toastLogin = Toast.makeText(getApplicationContext(), "Неправильный логин или пароль", Toast.LENGTH_LONG);
    }

    @Override
    public void onClick(View v) {
        //Создаём диологовое окно
        AlertDialog.Builder dialogEntry = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        final View mView = inflater.inflate(R.layout.dialog_entry, null);
        dialogEntry.setView(mView);
        dialogEntry.setNegativeButton("Отмена", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                dialog.cancel();
            }
        });
        dialogEntry.setPositiveButton("Вход", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                etLogin = (EditText)findViewById(R.id.etLogin);
                etPassword = (EditText)findViewById(R.id.etPassword);
                loginBtn();
            }
        });
        AlertDialog dial = dialogEntry.create();


        switch (v.getId()){
            case R.id.LLOtdBanks:
                Intent iGPSBanks = new Intent(this, GPSBanks.class);
                startActivity(iGPSBanks);
                break;
            case R.id.LLexchangeRates:
                Toast.makeText(this, "btn2", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnEntry:
                dial.show();
                break;
        }
    }


    public void loginBtn (){
        log = etLogin.getText().toString();
        pass = etPassword.getText().toString();


        Account activUser = dbHelper.login(log, pass);
        if(activUser != null){
            Intent iHome = new Intent(StartScreen.this, Home.class);
            iHome.putExtra("account", activUser);
            startActivity(iHome);
        }
        else {
            toastLogin.show();
        }
    }


    public void setUser(){
        contentValues.put(DBHelper.KEY_LOGIN, "qwerty");
        contentValues.put(DBHelper.KEY_PASSWORD, "qwerty");
        contentValues.put(DBHelper.KEY_NAME, "Илья");
        contentValues.put(DBHelper.KEY_SURNAME, "Бровцин");
        contentValues.put(DBHelper.KEY_PATRONYMIC, "Игоревич");
        database.insert(DBHelper.TABLE_NAME, null, contentValues);
    }
}
