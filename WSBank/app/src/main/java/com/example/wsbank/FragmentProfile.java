package com.example.wsbank;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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
import androidx.fragment.app.Fragment;

import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;

public class FragmentProfile extends Fragment implements View.OnClickListener {
    SharedPreferences sp;
    String NAME_TABL_SHAREDPREFERENCES = "WSBank";
    String FIRSTNAME = "firstname";
    String MIDDLENAME = "middlename";
    String NAME_TOKEN = "token";
    Context ctx;
    GetHttp getHttp = new GetHttp();
    LinearLayout btnExit, btnChangePassword, btnChangeLogin, btnInformation;

    AlertDialog alertDialog;
    View mView;
    TextView tvTitle, tvSubTitle;
    EditText etText;
    Button btnChanged, btnCencel;
    Boolean passOrLog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vFragmentProfile = inflater.inflate(R.layout.activity_profile, null);

        sp = ((HomeActivity)getActivity()).getSharedPreferences(NAME_TABL_SHAREDPREFERENCES, MODE_PRIVATE);
        String firstname = sp.getString(FIRSTNAME, "");
        String middlename = sp.getString(MIDDLENAME, "");

        ((HomeActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ctx = getContext();


        btnExit = vFragmentProfile.findViewById(R.id.btnExit);
        btnExit.setOnClickListener(this);
        btnChangePassword = vFragmentProfile.findViewById(R.id.btnChangePassword);
        btnChangePassword.setOnClickListener(this);
        btnChangeLogin = vFragmentProfile.findViewById(R.id.btnChangeLogin);
        btnChangeLogin.setOnClickListener(this);
        btnInformation = vFragmentProfile.findViewById(R.id.btnInformation);
        btnInformation.setOnClickListener(this);

        dialogCreate();

        return vFragmentProfile;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.findItem(R.id.profileMenu).setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnExit:
                logout();
                break;

            case R.id.btnChangePassword:
                alertDialog.show();
                tvTitle.setText(getResources().getString(R.string.change_password));
                tvSubTitle.setText(getResources().getString(R.string.set_new_password));
                etText.setText("");
                etText.setHint(getResources().getString(R.string.password));
                etText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                passOrLog = true;
                break;

            case R.id.btnChangeLogin:
                alertDialog.show();
                tvTitle.setText(getResources().getString(R.string.change_login));
                tvSubTitle.setText(getResources().getText(R.string.set_new_login));
                etText.setText("");
                etText.setHint(getResources().getString(R.string.login));
                etText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                passOrLog = false;
                break;

            case R.id.btnCencel:
                alertDialog.dismiss();
                break;

            case R.id.btnChanged:
                String text = String.valueOf(etText.getText());
                if(passOrLog) {
                    if (text.length() > 4) {
                        putChange(text);
                    } else {
                        Toast.makeText(ctx, "Введите пароль не меньше 5 символов", Toast.LENGTH_LONG).show();
                    }
                } else {
                    if(text.length() > 1){
                        putChange(text);
                    } else {
                        Toast.makeText(ctx, "Введите имя не меньше 2 символов", Toast.LENGTH_LONG).show();
                    }
                }
                alertDialog.dismiss();
                break;
        }
    }


    public void logout(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = "http://awh2.ddns.net:16482/logout";
                    String token = sp.getString(NAME_TOKEN, "");
                    String delRequedt = "{ \"token\": \"" +  token + "\" }";
                    String res = getHttp.http_delete(url, delRequedt);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent iHome = new Intent(ctx, HomeActivity.class);
                            startActivity(iHome);
                        }
                    });
                } catch (Exception e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ctx, "Ошибка", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }).start();
    }


    public void dialogCreate(){
        AlertDialog.Builder adBuider = new AlertDialog.Builder(ctx);
        LayoutInflater inflater = getLayoutInflater();
        mView = inflater.inflate(R.layout.dialog_change_user, null);
        adBuider.setView(mView);
        alertDialog = adBuider.create();

        tvTitle = mView.findViewById(R.id.tvTitle);
        tvSubTitle = mView.findViewById(R.id.tvSubTitle);
        etText = mView.findViewById(R.id.etText);
        btnChanged = mView.findViewById(R.id.btnChanged);
        btnChanged.setOnClickListener(this);
        btnCencel = mView.findViewById(R.id.btnCencel);
        btnCencel.setOnClickListener(this);
    }


    public void putChange(final String text){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String token = sp.getString(NAME_TOKEN, "");
                    String url, postRequest;
                    final String strMessage;

                    if(passOrLog) {
                        url = "http://awh2.ddns.net:16482/editepassword";
                        postRequest = "{ \"token\": \"" + token + "\",  \"password\": \"" + text + "\" }";
                        strMessage = "Пароль успешно изменён";
                    }
                    else {
                        url = "http://awh2.ddns.net:16482/editeusername";
                        postRequest = "{ \"token\": \"" + token + "\",  \"username\": \"" + text + "\" }";
                        strMessage = "Логин успешно изменён";
                    }

                    String res = getHttp.http_put(url, postRequest);
                    JSONObject root = new JSONObject(res);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ctx, strMessage, Toast.LENGTH_LONG).show();
                        }
                    });


                } catch (Exception e){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ctx, "Произошла ошибка", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }).start();
    }


}
