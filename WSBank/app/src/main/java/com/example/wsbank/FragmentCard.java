package com.example.wsbank;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GestureDetectorCompat;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;

public class FragmentCard extends Fragment implements View.OnClickListener {

    Context ctx;

    private LinearLayout btnHistoryOperations, btnBlock;
    private Button btnReplenish, btnTransfer;
    private TextView tvNumCard, tvNameCard, tvCashCard, tvBlockCard;

    private AlertDialog alertDialog;
    private TextView tvTitle, tvSubTitle;
    private EditText etText;
    private Button btnChanged, btnCencel;

    private String number, name;
    private Boolean block;

    private String NAME_TABL_SHAREDPREFERENCES = "WSBank";
    private String NAME_TOKEN = "token";
    private GetHttp getHttp = new GetHttp();

    private GestureDetectorCompat lSwipe;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vFragmentCard = inflater.inflate(R.layout.fragment_card, null);
        ((HomeActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ctx = getContext();

        tvNumCard = vFragmentCard.findViewById(R.id.tvNumCard);
        tvNameCard = vFragmentCard.findViewById(R.id.tvNameCard);
        tvCashCard = vFragmentCard.findViewById(R.id.tvCashCard);
        tvBlockCard = vFragmentCard.findViewById(R.id.tvBlockCard);

        Bundle bundle = getArguments();
        number = bundle.getString("nomer");
        name = bundle.getString("name");
        tvNameCard.setText(name);
        postLastNextCard(0);

        btnReplenish = vFragmentCard.findViewById(R.id.btnReplenish);
        btnReplenish.setOnClickListener(this);
        btnTransfer = vFragmentCard.findViewById(R.id.btnTransfer);
        btnTransfer.setOnClickListener(this);

        btnHistoryOperations = vFragmentCard.findViewById(R.id.btnHistoryOperations);
        btnHistoryOperations.setOnClickListener(this);
        btnBlock = vFragmentCard.findViewById(R.id.btnBlock);
        btnBlock.setOnClickListener(this);

        dialogCreate();

        LinearLayout fragment = vFragmentCard.findViewById(R.id.liner);
        lSwipe = new GestureDetectorCompat(ctx, new MyGusterListener());
        fragment.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return lSwipe.onTouchEvent(event);
            }
        });

        return vFragmentCard;
    }


    public class MyGusterListener extends GestureDetector.SimpleOnGestureListener{
        private static final int SWIPE_MIN_DISTANCE = 300;

        @Override
        public boolean onDown(MotionEvent event){
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE){
                Toast.makeText(ctx, "right", Toast.LENGTH_SHORT).show();
                postLastNextCard(+1);
            }
            if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE) {
                Toast.makeText(ctx, "left", Toast.LENGTH_SHORT).show();
                postLastNextCard(-1);
            }
            return false;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnHistoryOperations:
                Toast.makeText(getActivity(), "HO", Toast.LENGTH_LONG).show();
                break;

            case R.id.btnBlock:
                alertDialog.show();
                tvTitle.setText(getResources().getString(R.string.block_card));
                tvSubTitle.setText(getResources().getString(R.string.block_card_question));
                etText.setText("");
                etText.setHint(getResources().getString(R.string.password));
                etText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                btnChanged.setText(getResources().getString(R.string.block));
                break;

            case R.id.btnReplenish:
                Toast.makeText(getActivity(), "Replenish", Toast.LENGTH_LONG).show();
                break;

            case R.id.btnTransfer:
                Toast.makeText(getActivity(), "Transfer", Toast.LENGTH_LONG).show();
                break;

            case R.id.btnCencel:
                alertDialog.dismiss();
                break;

            case R.id.btnChanged:
                postBlockCard();
                alertDialog.dismiss();
                break;
        }
    }


    public  void dialogCreate(){
        AlertDialog.Builder adBuilder = new AlertDialog.Builder(ctx);
        LayoutInflater inflater = getLayoutInflater();
        View mView = inflater.inflate(R.layout.dialog_change_user, null);
        adBuilder.setView(mView);
        alertDialog = adBuilder.create();

        tvTitle = mView.findViewById(R.id.tvTitle);
        tvSubTitle = mView.findViewById(R.id.tvSubTitle);
        etText = mView.findViewById(R.id.etText);
        btnChanged = mView.findViewById(R.id.btnChanged);
        btnChanged.setOnClickListener(this);
        btnCencel = mView.findViewById(R.id.btnCencel);
        btnCencel.setOnClickListener(this);
    }


    public void postBlockCard(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = "http://awh2.ddns.net:16482/block";
                    SharedPreferences sp = getActivity().getSharedPreferences(NAME_TABL_SHAREDPREFERENCES, MODE_PRIVATE);
                    String strToken = sp.getString(NAME_TOKEN, "");
                    String postRequest = "{ \"token\": \"" + strToken + "\", \"card_number\": \"" + number + "\" }";
                    final String res = getHttp.http_post(url, postRequest);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            postLastNextCard(0);
                        }
                    });


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    public void postLastNextCard(final int a){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = "http://awh2.ddns.net:16482/getcards";
                    SharedPreferences sp = getActivity().getSharedPreferences(NAME_TABL_SHAREDPREFERENCES, MODE_PRIVATE);
                    String token = sp.getString(NAME_TOKEN, "");
                    String postRequest = "{ \"token\": \"" + token + "\" }";
                    final String resCard = getHttp.http_post(url, postRequest);

                    url = "http://awh2.ddns.net:16482/getcheck";
                    final String resBill = getHttp.http_post(url, postRequest);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String numCard = "";
                            String numBill = "";
                            String cashCard = "";
                            JSONArray root = null, root1 = null;

                            try {
                                root = new JSONArray(resCard);
                                root1 = new JSONArray(resBill);
                            } catch (JSONException e) { }

                                for(int i = 0; i < root.length(); i++){
                                    JSONObject item = null;
                                    try {
                                        item = root.getJSONObject(i);
                                        numCard = item.getString("card_number");

                                        if(Double.parseDouble(numCard) == Double.parseDouble(number)){
                                            item = root.getJSONObject(i + a);
                                            numCard = item.getString("card_number");
                                            numBill = item.getString("check_number");
                                            block = item.getBoolean("block");
                                            blockUnblock();

                                            for(int j = 0; j < root1.length(); j++){
                                                JSONObject item1 = root1.getJSONObject(j);

                                                if(Double.parseDouble(numBill) == Double.parseDouble(item1.getString("check_number"))){
                                                    number = numCard;
                                                    cashCard = item1.getString("balance");
                                                    tvNumCard.setText(numCard);
                                                    tvCashCard.setText(cashCard);
                                                    return;
                                                }
                                            }
                                            return;
                                        }
                                    } catch (JSONException e) {
                                        postLastNextCard(a * (-1));
                                    }

                                }
                        }
                    });


                } catch (Exception e) { }
            }
        }).start();
    }


    public void blockUnblock(){
        if(block){
            tvBlockCard.setVisibility(View.VISIBLE);
        } else {
            tvBlockCard.setVisibility(View.INVISIBLE);
        }
    }


}