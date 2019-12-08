package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ListView lst;
    Context ctx;
    ArrayAdapter<note_info> adp;
    Button btnSetNote;
    AlertDialog.Builder builder;
    View mView;
    EditText etHours, etMinute;
    Button btnAdd, btnCencel;
    AlertDialog alertDialog;
    CalendarView calendarView;
    int Year, Mounth, Day;


    class note_info {
        public int id;
        public String title;
        public String content;

        public note_info(int i, String t, String c){
            id = i;
            title = t;
            content = c;
        }

        public String toString(){
            return title;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSetNote = (Button)findViewById(R.id.btnSetNote);
        btnSetNote.setOnClickListener(this);
        ctx = this;
        lst = (ListView)findViewById(R.id.myList);

        adp = new ArrayAdapter<note_info>(this, android.R.layout.simple_list_item_1);
        lst.setAdapter(adp);

        lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                note_info x = adp.getItem(position);
                Toast.makeText(ctx, String.valueOf(x.id) + " -- " + x.title + " -- " + x.content, Toast.LENGTH_LONG).show();
            }
        });

        update_notes();
    }

    void update_notes(){
        ApiHelper x = new ApiHelper(this){
            public void on_ready(String res){
                try {
                    JSONObject root = new JSONObject(res);
                    boolean result = root.getBoolean("result");
                    final JSONArray list = root.getJSONArray("list");
                    adp.clear();
                    for(int i = 0; i < list.length(); i++){
                        JSONObject item = list.getJSONObject(i);
                        int id = item.getInt("id");
                        String title = item.getString("title");
                        String content = item.getString("content");
                        adp.add(new note_info(id, title, content));
                    }
                    adp.notifyDataSetInvalidated();
                    Toast.makeText(ctx, res, Toast.LENGTH_LONG).show();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        x.send("http://localhost:8080/api/get-note-list");


        builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        mView = inflater.inflate(R.layout.add_note, null);
        builder.setView(mView);
        alertDialog = builder.create();

        etHours = mView.findViewById(R.id.etHours);
        etMinute = mView.findViewById(R.id.etMinute);
        btnAdd =  mView.findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);
        btnCencel =  mView.findViewById(R.id.btnCencel);
        btnCencel.setOnClickListener(this);

        calendarView = mView.findViewById(R.id.cvDate);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Year = year;
                Mounth = month;
                Day = dayOfMonth;
            }
        });
        calendarView.setMinDate(Calendar.getInstance().getTimeInMillis());
    }

    public void on_add_click(View v){
        ApiHelper x = new ApiHelper(this){
            public void on_ready(String res){
                Toast.makeText(ctx, res, Toast.LENGTH_LONG).show();

                try {
                    JSONObject root = new JSONObject(res);
                    boolean result = root.getBoolean("result");
                    int id = root.getInt("id");
                    update_notes();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        x.send("http://localhost:8080/api/add-note");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSetNote:
                alertDialog.show();

                Calendar calendar = Calendar.getInstance();
                String minute = Integer.toString(calendar.get(Calendar.MINUTE));
                String hour = Integer.toString(calendar.get(Calendar.HOUR_OF_DAY));

                etHours.setHint(hour);
                etMinute.setHint(minute);
                TextWatcherP twpHours = new TextWatcherP(etHours, 23);
                TextWatcherP twpMinute = new TextWatcherP(etMinute, 59);
                etHours.addTextChangedListener(twpHours);
                etMinute.addTextChangedListener(twpMinute);
                break;

            case R.id.btnCencel:
                alertDialog.dismiss();
                break;

            case R.id.btnAdd:
                ApiHelper x = new ApiHelper(this){
                    public void on_ready(String res){
                        Toast.makeText(ctx, res, Toast.LENGTH_LONG).show();

                        try {
                            JSONObject root = new JSONObject(res);
                            boolean result = root.getBoolean("result");
                            int id = root.getInt("id");
                            update_notes();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                x.send("http://localhost:8080/api/add-note");
                alertDialog.dismiss();
                break;
        }
    }
}
