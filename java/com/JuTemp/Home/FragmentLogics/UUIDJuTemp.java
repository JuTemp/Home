package com.JuTemp.Home.FragmentLogics;

import android.app.Activity;
import android.view.View;

import androidx.appcompat.widget.AppCompatEditText;

import com.JuTemp.Home.R;
import com.JuTemp.Home.util.FragmentLogicJuTemp;
import com.JuTemp.Home.util.UUIDGenerateJuTemp;

public class UUIDJuTemp extends FragmentLogicJuTemp {
    @Override
    public void mainLogic(Activity ThisActivity, FragmentLogicJuTemp fragmentLogic, View view) {
        super.mainLogic(ThisActivity, fragmentLogic, view);

        view.findViewById(R.id.uuid_exec).setOnClickListener(v -> {
            ((AppCompatEditText) view.findViewById(R.id.uuid_uuid_dash)).setText(UUIDGenerateJuTemp.generateUUID(true));
            ((AppCompatEditText) view.findViewById(R.id.uuid_uuid)).setText(UUIDGenerateJuTemp.generateUUID(false));
            ((AppCompatEditText) view.findViewById(R.id.uuid_longuuid)).setText(UUIDGenerateJuTemp.generateShortUUID(32));
            ((AppCompatEditText) view.findViewById(R.id.uuid_shortuuid)).setText(UUIDGenerateJuTemp.generateShortUUID(8));
        });

    }
}
