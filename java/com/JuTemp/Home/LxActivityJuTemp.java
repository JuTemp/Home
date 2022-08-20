package com.JuTemp.Home;

import android.app.Activity;
import android.content.Intent;

public class LxActivityJuTemp extends Activity {

    LxActivityJuTemp This = this;

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = new Intent();
        intent.setClassName("com.longshine.smartcity", "com.longshine.smartcity.mvp.ui.main.activity.MainActivity");
        This.startActivity(intent);

    }

    @Override
    protected void onResume() {
        super.onResume();
        This.finish();
    }
}

