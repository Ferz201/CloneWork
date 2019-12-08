package com.example.wsbank;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class BoxAdapter2 extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<ItemsList> objects;

    BoxAdapter2(Context context, ArrayList<ItemsList> items){
        ctx = context;
        objects = items;
        lInflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // используем созданные, но не используемые view
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.list_item2, parent, false);
        }

        ItemsList p = getItemList(position);

        // заполняем View в пункте списка данными из товаров: наименование, цена
        // и картинка
        ((TextView) view.findViewById(R.id.tvName)).setText(p.name);
        ((TextView) view.findViewById(R.id.tvNomer)).setText(p.nomers);
        ((TextView) view.findViewById(R.id.tvCash)).setText(p.cash);

        return view;
    }

    ItemsList getItemList(int position) {
        return ((ItemsList) getItem(position));
    }
}
