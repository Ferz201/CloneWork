package com.example.ilya.bank;

import android.content.res.ColorStateList;
import android.graphics.Color;

import static android.graphics.Color.*;

/**
 * Created by Ilya on 18.10.2019.
 */

public class DateBank {
    String nameBank;
    String bankomat;
    String time;
    String work;
    int clr;


    DateBank(String _nameBank, String _bankomat, String _time, String _work, int _clr) {

        nameBank = _nameBank;
        bankomat = _bankomat;
        time = _time;
        work = _work;
        clr = _clr;
    }
}
