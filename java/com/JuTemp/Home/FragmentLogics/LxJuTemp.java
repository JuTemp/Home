package com.JuTemp.Home.FragmentLogics;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.JuTemp.Home.ApplJuTemp;
import com.JuTemp.Home.HomeJuTemp;
import com.JuTemp.Home.R;
import com.JuTemp.Home.util.FragmentLogicJuTemp;
import com.JuTemp.Home.util.ShortcutJuTemp;

public class LxJuTemp extends FragmentLogicJuTemp {

    @Override
    public void mainLogic(Activity ThisActivity, FragmentLogicJuTemp fragmentLogic, View view) {
        super.mainLogic(ThisActivity, fragmentLogic, view);

        (view.findViewById(R.id.lx_shortcut_add)).setOnClickListener(v -> shortcutAdd());
        (view.findViewById(R.id.lx_shortcut_del)).setOnClickListener(v -> shortcutDel());

    }

    @Override
    public void actionbarMenuItemClickListener(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add:
                shortcutAdd();
                break;
            case R.id.menu_del:
                shortcutDel();
                break;
            default:
                break;
        }
    }

    private void shortcutAdd() {
        Bundle bundle = new Bundle();
        bundle.putString("key", "lx");
        ShortcutJuTemp.shortcutAdd(ThisActivity, bundle, "lx", R.string.lx_short_label, R.string.lx_long_label, R.drawable.lx);

    }

    private void shortcutDel() {
        ShortcutJuTemp.shortcutDel(ThisActivity);
    }
}
