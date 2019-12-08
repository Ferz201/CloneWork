package com.example.ilya.bank;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Ilya on 18.10.2019.
 */

public class BoxAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<DateBank> objects;

    BoxAdapter(Context context, ArrayList<DateBank> dateBanks) {
        ctx = context;
        objects = dateBanks;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    // кол-во элементов
    @Override
    public int getCount() {
        return objects.size();
    }

    // элемент по позиции
    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    // id по позиции
    @Override
    public long getItemId(int position) {
        return position;
    }

    // пункт списка
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // используем созданные, но не используемые view
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.item_list_banks, parent, false);
        }

        DateBank p = getProduct(position);

        ((TextView) view.findViewById(R.id.tvNameBank)).setText(p.nameBank);
        ((TextView) view.findViewById(R.id.tvBankomat)).setText(p.bankomat);
        ((TextView) view.findViewById(R.id.tvTime)).setText(p.time);
        TextView work3 = (TextView)view.findViewById(R.id.tvWork);
        work3.setText(p.work);
        work3.setTextColor(p.clr);

        //((TextView) view.findViewById(R.id.tvWork)).setText(p.work);

        return view;
    }

    // товар по позиции
    DateBank getProduct(int position) {
        return ((DateBank) getItem(position));
    }
}
