package com.example.notes;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class TextWatcherP implements TextWatcher {
    public EditText editText;
    public int limit;

    public TextWatcherP(EditText et, int lim){
        super();
        editText = et;
        limit = lim;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        try{
            Integer integer = Integer.parseInt(editText.getText().toString());
            if(integer > limit){
                editText.setText(editText.getText().subSequence(0, editText.getText().length()-1));
                editText.setSelection(editText.getText().length());
            }
        } catch (Exception e) {

        }
    }
}
