package com.JuTemp.Home.util;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.widget.Toast;

import com.JuTemp.Home.HomeJuTemp;
import com.JuTemp.Home.LxActivityJuTemp;
import com.JuTemp.Home.R;

public class ShortcutJuTemp {

    public static void shortcutAdd(Activity ThisActivity, Bundle bundle, String idString, int shortLabelId, int longLabelId, int iconId)
    {
        ShortcutManager shortcutManager = ThisActivity.getSystemService(ShortcutManager.class);
        if (shortcutManager == null || !shortcutManager.isRequestPinShortcutSupported()) return;
        Intent intent = new Intent(ThisActivity, LxActivityJuTemp.class);
        intent.setAction(Intent.ACTION_VIEW);
        intent.putExtras(bundle);
        ShortcutInfo pinShortcutInfo = new ShortcutInfo.Builder(ThisActivity, idString)
                .setShortLabel(ThisActivity.getString(shortLabelId))
                .setLongLabel(ThisActivity.getString(longLabelId))
                .setIcon(Icon.createWithResource(ThisActivity, iconId))
                .setIntent(intent)
                .build();
        Intent pinnedShortcutCallbackIntent = shortcutManager.createShortcutResultIntent(pinShortcutInfo);
        PendingIntent successCallback = PendingIntent.getBroadcast(ThisActivity, 0,
                pinnedShortcutCallbackIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        shortcutManager.requestPinShortcut(pinShortcutInfo, successCallback.getIntentSender());
    }

    public static void shortcutDel(Activity ThisActivity)
    {
        Toast.makeText(ThisActivity, R.string.shortdel_desc, Toast.LENGTH_LONG).show();
    }
}
