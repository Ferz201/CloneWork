package com.example.ilya.bank;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Ilya on 30.10.2019.
 */

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "Bank";
    public static final String TABLE_NAME = "users";

    public static final String KEY_ID = "_id";
    public static final String KEY_LOGIN = "login";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_NAME = "name";
    public static final String KEY_SURNAME = "surname";
    public static final String KEY_PATRONYMIC = "patronymic";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (" + KEY_ID + " integer primary key, " + KEY_LOGIN + " text, " + KEY_PASSWORD + " text, "
        + KEY_NAME + " text, " + KEY_SURNAME + " text, " + KEY_PATRONYMIC + " text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_NAME);

        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_NAME);

        onCreate(db);
    }


    public Account login(String login, String password){
        Account account = null;
        try{
            SQLiteDatabase sqLiteDatabase = getReadableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery("select * from " + TABLE_NAME + " where " + KEY_LOGIN + "=? and " + KEY_PASSWORD + "=?", new String[]{login, password});
            if(cursor.moveToFirst()){
                account = new Account();
                account.setId(cursor.getInt(0));
                account.setLogin(cursor.getString(1));
                account.setPassword(cursor.getString(2));
                account.setName(cursor.getString(3));
                account.setSurname(cursor.getString(4));
                account.setPatronymic(cursor.getString(5));
            }
        }
        catch (Exception e){
            account = null;
        }

        return account;
    }
}
