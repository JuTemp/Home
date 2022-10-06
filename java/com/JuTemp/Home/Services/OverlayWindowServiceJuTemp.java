package com.JuTemp.Home.Services;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.LifecycleService;

import com.JuTemp.Home.R;

// https://blog.csdn.net/qq_37750825/article/details/115754647

public class OverlayWindowServiceJuTemp extends LifecycleService {

    OverlayWindowServiceJuTemp This = this;

    static WindowManager windowManager = null;
    static View overlayWindow = null;
    static WindowManager.LayoutParams layoutParams = null;

    @Override
    public void onCreate() {
        super.onCreate();

        windowManager = (WindowManager) This.getSystemService(WINDOW_SERVICE);
        layoutParams = new WindowManager.LayoutParams();
        {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            layoutParams.format = PixelFormat.RGBA_8888;
            layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            layoutParams.gravity = Gravity.CENTER;
            layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        }
        overlayWindow = LayoutInflater.from(This).inflate(R.layout.overlay_window_view, null);
        overlayWindow.setOnTouchListener(new ItemViewTouchListener(layoutParams, windowManager));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int i = super.onStartCommand(intent, flags, startId);
        String content = intent.getStringExtra("content");
        if (!content.isEmpty()) ((AppCompatTextView) overlayWindow.findViewById(R.id.overlaywindow_text)).setText(content);
        windowManager.addView(overlayWindow, layoutParams);
        return i;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        windowManager.removeView(overlayWindow);
    }

    private static class ItemViewTouchListener implements View.OnTouchListener {

        private int x = 0, y = 0;
        private WindowManager.LayoutParams layoutParams = null;
        private WindowManager windowManager = null;

        public ItemViewTouchListener(WindowManager.LayoutParams layoutParams, WindowManager windowManager) {
            this.layoutParams = layoutParams;
            this.windowManager = windowManager;
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x = (int) motionEvent.getRawX();
                    y = (int) motionEvent.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    int nowX = (int) motionEvent.getRawX();
                    int nowY = (int) motionEvent.getRawY();
                    int movedX = nowX - x;
                    int movedY = nowY - y;
                    x = nowX;
                    y = nowY;
                    layoutParams.x += movedX;
                    layoutParams.y += movedY;
                    windowManager.updateViewLayout(view, layoutParams);
                    break;
                default:
                    break;
            }
            return false;
        }
    }

    public static final Handler handler = new Handler(msg -> {
        String content = (String) msg.obj;
        ((AppCompatTextView) overlayWindow.findViewById(R.id.overlaywindow_text)).setText(content);
        windowManager.updateViewLayout(overlayWindow, layoutParams);
        return true;
    });
}
