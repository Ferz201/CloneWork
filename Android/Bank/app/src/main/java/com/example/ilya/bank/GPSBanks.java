package com.example.ilya.bank;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import static com.example.ilya.bank.R.layout.item_list_banks;

public class GPSBanks extends Activity {

    String otdOrBank, timeBank, workBank;
    Random rnd = new Random();
    int ran;
    DateFormat dateFormat = new SimpleDateFormat("HH");
    Date date = new Date();
    int hour = Integer.parseInt(dateFormat.format(date));
    TextView work;
    int iClr;
    ListView lvBanks;

    ArrayList<DateBank> dateBanks = new ArrayList<DateBank>();
    BoxAdapter boxAdapter;

    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpsbanks);

        // создаем адаптер
        fillData();
        boxAdapter = new BoxAdapter(this, dateBanks);

        // настраиваем список
        ListView lvMain = (ListView) findViewById(R.id.lvBanks);
        lvMain.setAdapter(boxAdapter);

        lvBanks = (ListView)findViewById(R.id.lvBanks);

        lvBanks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id) {
                TextView textView = (TextView)itemClicked.findViewById(R.id.tvNameBank);
                String txt = textView.getText().toString();
                Toast.makeText(getApplicationContext(), txt, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // генерируем данные для адаптера
    void fillData() {
        work = (TextView)findViewById(R.id.tvWork);

        for (int i = 1; i <= 20; i++) {
            ran = rnd.nextInt(2);
            if(ran == 0) {
                otdOrBank = "Отделение";

                timeBank = "10.00-22.00";
                if(hour >= 10 && hour < 22){
                    workBank = "Работает";
                    iClr = Color.parseColor("#3bb300");
                }
                else {
                    workBank = "Закрыто";
                    iClr = Color.parseColor("#e60000");
                }
            }
            else {
                otdOrBank = "Банкомат";
                timeBank = "00.00-00.00";
                workBank = "Работает";
                iClr = Color.parseColor("#3bb300");
            }

            dateBanks.add(new DateBank("Москва, ул.Вавилова, д." + i, otdOrBank, "Часы работы: " + timeBank, workBank, iClr));
        }
    }
}
