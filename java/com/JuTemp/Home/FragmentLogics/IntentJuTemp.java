package com.JuTemp.Home.FragmentLogics;

import android.app.*;
import android.view.*;
import android.widget.*;

import com.JuTemp.Home.*;
import com.JuTemp.Home.FragmentFramework.FragmentLogicJuTemp;

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

        requestFocus(ThisActivity, (EditText) view.findViewById(R.id.intent_data));

        view.findViewById(R.id.intent_exec).setOnClickListener(p1 -> {
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
        });
        view.findViewById(R.id.intent_exec).setOnLongClickListener(view1 -> {
            try {
                Intent intent = new Intent();
                String strUrl = ((EditText) view1.findViewById(R.id.intent_data)).getText().toString();
                intent.setData(Uri.parse(strUrl));
                ThisActivity.startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(ThisActivity, Re.getString(R.string.intent_error), Toast.LENGTH_LONG).show();
            }
            return true;
        });

    }

}
