package com.JuTemp.Home.FragmentLogics;

import android.app.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;

import com.JuTemp.Home.*;
import com.JuTemp.Home.util.*;

import android.content.*;
import android.net.*;

public class IntentJuTemp extends FragmentLogicJuTemp {
    Activity ThisActivity = null;
    IntentJuTemp This = null;
    View view = null;

    @Override
    public void mainLogic(final Activity ThisActivity, FragmentLogicJuTemp This, final View view) {
        this.ThisActivity = ThisActivity;
        this.This = (IntentJuTemp) This;
        this.view = view;

        super.requestFocus(ThisActivity, (EditText) view.findViewById(R.id.intent_data));

        ((Button) view.findViewById(R.id.intent_exec)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View p1) {
                try {
                    Intent intent = new Intent();
                    String strUrl = ((EditText) view.findViewById(R.id.intent_data)).getText().toString();
                    intent.setData(Uri.parse(strUrl));
                    if (strUrl.startsWith("http://") || strUrl.startsWith("https://"))
                        intent.setClassName("mark.via", "mark.via.Shell");
                    ThisActivity.startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(ThisActivity, Re.getString(R.string.intent_error), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}
