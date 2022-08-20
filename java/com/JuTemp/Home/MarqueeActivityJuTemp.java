package com.JuTemp.Home;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MarqueeActivityJuTemp extends Activity {

    MarqueeActivityJuTemp This=this;
//    String content=null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.marquee_activity);

        String content=This.getIntent().getStringExtra("content");
//        Toast.makeText(This,content,Toast.LENGTH_LONG).show();
        TextView tvMarquee=(TextView) This.findViewById(R.id.marquee_activity_tv);
        tvMarquee.setText(content);
    }
}
