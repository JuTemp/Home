package com.JuTemp.Home.FragmentLogics;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;

import androidx.appcompat.widget.AppCompatEditText;

import com.JuTemp.Home.Activities.ApplJuTemp;
import com.JuTemp.Home.Activities.HomeJuTemp;
import com.JuTemp.Home.Services.OverlayWindowServiceJuTemp;
import com.JuTemp.Home.R;
import com.JuTemp.Home.FragmentFramework.FragmentLogicJuTemp;
import com.JuTemp.Home.util.MessageJuTemp;
import com.JuTemp.Home.util.NotificationJuTemp;

import java.util.List;
import java.util.Objects;

public class OverlayWindowJuTemp extends FragmentLogicJuTemp {

    OverlayWindowJuTemp This = this;

    Intent intentService = null;
    Intent intentNotification = null;
    SharedPreferences sp = null;
    SharedPreferences.Editor spe = null;


    final String ServiceName = "com.JuTemp.Home.Services.OverlayWindowServiceJuTemp";

    @Override
    public void mainLogic(Activity ThisActivity, FragmentLogicJuTemp fragmentLogic, View view) {
        super.mainLogic(ThisActivity, fragmentLogic, view);

        requestFocus(ThisActivity, view.findViewById(R.id.overlaywindow_edittext));

        sp = ThisActivity.getSharedPreferences(ApplJuTemp.OVERLAY_WINDOW_SAVING, Context.MODE_PRIVATE);
        spe = sp.edit();

        intentService = new Intent(ThisActivity, OverlayWindowServiceJuTemp.class);
        intentNotification = new Intent(ThisActivity, HomeJuTemp.class);
        intentNotification.putExtra("index", 0); //

        for (int i : new int[]{R.id.overlaywindow_start_ow, R.id.overlaywindow_stop_ow, R.id.overlaywindow_update})
            view.findViewById(i).setOnClickListener(ocl);
        ((AppCompatEditText) view.findViewById(R.id.overlaywindow_edittext)).setText(sp.getString("content", null));
    }

    private final View.OnClickListener ocl = v -> {
        String content = Objects.requireNonNull(((AppCompatEditText) view.findViewById(R.id.overlaywindow_edittext)).getText()).toString();
        if (content.isEmpty()) content = Re.getString(R.string.overlaywindow_text_default);
        switch (v.getId()) {
            case R.id.overlaywindow_start_ow:
                if (isActive(ThisActivity, ServiceName)) return;
                spe.putString("content", content);
                spe.apply();
                intentService.putExtra("content", content);
                ThisActivity.startService(intentService);
                NotificationJuTemp.sendNotification(ThisActivity, intentNotification, ApplJuTemp.NOTIFICATION_ID, R.drawable.ic_launcher_tp, Re.getString(R.string.notification_title), Re.getString(R.string.notification_text));
                break;
            case R.id.overlaywindow_stop_ow:
                if (!isActive(ThisActivity, ServiceName)) return;
                if (sp.getString("content", null) != null) {
                    spe.remove("content");
                    spe.apply();
                }
                ThisActivity.stopService(intentService);
                NotificationJuTemp.cancelNotification(ApplJuTemp.NOTIFICATION_ID);
                break;
            case R.id.overlaywindow_update:
                if (!isActive(ThisActivity, ServiceName)) {
                    intentService.putExtra("content", content);
                    ThisActivity.startService(intentService);
                    NotificationJuTemp.sendNotification(ThisActivity, intentNotification, ApplJuTemp.NOTIFICATION_ID, R.drawable.notifi, Re.getString(R.string.notification_title), Re.getString(R.string.notification_text));
                }
                spe.putString("content", content);
                spe.apply();
                OverlayWindowServiceJuTemp.handler.sendMessage(MessageJuTemp.String2Message(content));
                break;
            default:
                break;
        }
    };

    private boolean isActive(Context context, String serviceName) {
        boolean activeOrNot = false;
        ActivityManager myAM = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(40);
        if (myList.size() <= 0) return false;
        for (ActivityManager.RunningServiceInfo rsi : myList) {
            String mName = rsi.service.getClassName();
            if (Objects.equals(mName, serviceName)) {
                activeOrNot = true;
                break;
            }
        }
        return activeOrNot;
    }
}
