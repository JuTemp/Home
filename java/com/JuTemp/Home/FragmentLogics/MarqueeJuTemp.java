package com.JuTemp.Home.FragmentLogics;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.JuTemp.Home.MarqueeActivityJuTemp;
import com.JuTemp.Home.R;
import com.JuTemp.Home.util.FragmentLogicJuTemp;

public class MarqueeJuTemp extends FragmentLogicJuTemp {

    Activity ThisActivity = null;
    MarqueeJuTemp This = null;
    View view = null;

    EditText textin = null;

    @Override
    public void mainLogic(Activity ThisActivity, FragmentLogicJuTemp fragmentLogic, View view) {

        this.ThisActivity = ThisActivity;
        this.This = (MarqueeJuTemp) fragmentLogic;
        this.view = view;

        textin = (EditText) view.findViewById(R.id.marquee_textin);
        requestFocus(ThisActivity, textin);

        ((Button) view.findViewById(R.id.marquee_exec)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = textin.getText().toString();
                Intent intent = new Intent(ThisActivity, MarqueeActivityJuTemp.class);
                intent.putExtra("content", content);
                ThisActivity.startActivity(intent);
            }
        });

    }
}
