package com.example.wsbank;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FragmentHome extends Fragment {
    Context ctx;
    BoxAdapter2 boxAdapterCard;
    BoxAdapter2 boxAdapterBills;
    BoxAdapter2 boxAdapterCredits;
    ArrayList<ItemsList> arrCards = new ArrayList<ItemsList>();
    ArrayList<ItemsList> arrBills = new ArrayList<ItemsList>();
    ArrayList<ItemsList> arrCredits = new ArrayList<ItemsList>();
    ListView lvCards;
    ListView lvBills;
    ListView lvCredits;
    String token;
    GetHttp getHttp = new GetHttp();
    Fragment fragmentCard;


    FragmentHome(String _token){
        token = _token;
    }


    @Nullable
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vFragmentHome = inflater.inflate(R.layout.fragment_home, null);
        ((HomeActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ctx = getContext();

        lvCards = vFragmentHome.findViewById(R.id.lvCards);
        lvBills = vFragmentHome.findViewById(R.id.lvBills);
        lvCredits = vFragmentHome.findViewById(R.id.lvCredits);

        postCard();
        lvCards.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ItemsList il = arrCards.get(position);
                String txtNomerCard = il.nomers;
                String txtNameCard = il.name;
                String txtCashCard = il.cash;

                fragmentCard = new FragmentCard();
                Bundle bundle = new Bundle();
                bundle.putString("nomer", txtNomerCard);
                bundle.putString("name", txtNameCard);
                bundle.putString("cash", txtCashCard);
                fragmentCard.setArguments(bundle);
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment, fragmentCard).addToBackStack(null).commit();
            }
        });


        return vFragmentHome;
    }

    public static void justifyListViewHeightBasedOnChildren (ListView listView) {

        ListAdapter adapter = listView.getAdapter();

        if (adapter == null) {
            return;
        }
        ViewGroup vg = listView;
        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, vg);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams par = listView.getLayoutParams();
        par.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(par);
        listView.requestLayout();
    }


    public void postCard(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = "http://awh2.ddns.net:16482/getcards";
                    String postRequest = "{ \"token\": \"" + token + "\" }";
                    final String resCard = getHttp.http_post(url, postRequest);

                    url = "http://awh2.ddns.net:16482/getcheck";
                    final String resBill = getHttp.http_post(url, postRequest);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String nameCard = "Дебетовая карта";
                            String nameBill = "Текущий счёт";
                            String numCard = "";
                            String checkNum = "";
                            String cash = "";
                            String itemNum = "";
                            try {
                                arrCards.clear();
                                arrBills.clear();

                                JSONArray root = new JSONArray(resCard);
                                JSONArray root1 = new JSONArray(resBill);

                                for(int i = 0; i < root.length(); i++){
                                    JSONObject item = root.getJSONObject(i);
                                    numCard = item.getString("card_number");
                                    checkNum = item.getString("check_number");
                                    double dCheckNum = Double.parseDouble(checkNum);

                                    for (int j = 0; j < root1.length(); j++){
                                        JSONObject item1 = root1.getJSONObject(j);
                                        itemNum = item1.getString("check_number");
                                        double dItemNum = Double.parseDouble(itemNum);

                                        if (dCheckNum == dItemNum) {
                                            cash = item1.getString("balance");
                                            arrCards.add(new ItemsList(nameCard, numCard, cash + " рублей"));
                                        }
                                    }
                                }
                                boxAdapterCard = new BoxAdapter2(ctx, arrCards);
                                lvCards.setAdapter(boxAdapterCard);
                                justifyListViewHeightBasedOnChildren(lvCards);

                                for (int i = 0; i < root1.length(); i++){
                                    JSONObject item1 = root1.getJSONObject(i);
                                    itemNum = item1.getString("check_number");
                                    cash = item1.getString("balance");
                                    arrBills.add(new ItemsList(nameBill, itemNum, cash + " рублей"));
                                }
                                boxAdapterBills = new BoxAdapter2(ctx, arrBills);
                                lvBills.setAdapter(boxAdapterBills);
                                justifyListViewHeightBasedOnChildren(lvBills);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
