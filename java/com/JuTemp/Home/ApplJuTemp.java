package com.JuTemp.Home;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;

public final class ApplJuTemp extends Application {

    public static final String URL_ARRAY = "URLArray";
    public static final String NGINX_ARRAY = "NginxArray";
    public static final String MEMO = "MEMO";
    public static final String START_ACTIVITY_INDEX="StartActivityIndex";
    public static final String OVERLAY_WINDOW_SAVING="OverlayWindow";
    public static final String DICT_MODE="DictMode";
    public static final String DICT_FONTSIZE="DictFontSize";
    public static final String DICT_APPID_SPF="DictAPPID";
    public static final String DICT_APPKEY_SPF="DictAPPKEY";
    public static final String BOOTALARMMEMO_FLAG="BootAlarmMemoFlag";
    public static final String BOOTALARMMEMO_CONTENT="BootAlarmMemoContent";
    public static final int NOTIFICATION_ID = 520;
    public static final String NOTIFICATION_CHANNEL = "520";
    public static String DICT_APPID = "";
    public static String DICT_APPKEY = "";
    public static final int FILE_SELECTOR_REQUEST_CODE = 520530;
    public static volatile String viaShareString = "";
    public static volatile boolean shareFlag = false;

    public static final String UA_ANDROID = System.getProperty("http.agent");// "Mozilla/5.0 (Linux; Android 9; HWI-AL00 Build/HUAWEIHWI-AL00; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/79.0.3945.116 Mobile Safari/537.36";
    public static final String UA_DESKTOP = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.198 Safari/537.36";
    public static final String UA_WECHAT="Mozilla/5.0 (Linux; Android 9; HWI-AL00 Build/HUAWEIHWI-AL00; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/86.0.4240.99 XWEB/4293 MMWEBSDK/20220604 Mobile Safari/537.36 MMWEBID/3517 MicroMessenger/8.0.24.2180(0x28001851) WeChat/arm64 Weixin NetType/WIFI Language/zh_CN ABI/arm64";
    public static final String UA_QQ="Mozilla/5.0 (Linux; Android 9; HWI-AL00 Build/HUAWEIHWI-AL00; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/53.0.2785.49 Mobile MQQBrowser/6.2 TBS/043128 Safari/537.36 V1_AND_SQ_7.0.0_676_YYB_D PA QQ/7.0.0.3135 NetType/4G WebP/0.3.0 Pixel/1080";
    public static boolean lxFlag=false;

    public static void requestFocus(final Activity ThisActivity, @NonNull final EditText editText) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                editText.clearFocus();
                editText.requestFocus();
                ApplJuTemp.showSoftInput((InputMethodManager) ThisActivity.getSystemService(Context.INPUT_METHOD_SERVICE), editText);
            }
        }, 300);
    }

    public static void showSoftInput(@NonNull InputMethodManager inputMethodManager, View view) {
        ApplJuTemp.showSoftInput(inputMethodManager,view, InputMethodManager.SHOW_IMPLICIT);
    }

    public static void hideSoftInput(@NonNull InputMethodManager inputMethodManager, IBinder windowToken) {
        ApplJuTemp.hideSoftInput(inputMethodManager,windowToken, 0);
    }

    public static void showSoftInput(@NonNull InputMethodManager inputMethodManager, View view, int flag) {
        inputMethodManager.showSoftInput(view, flag);
    }

    public static void hideSoftInput(@NonNull InputMethodManager inputMethodManager, IBinder windowToken, int flag) {
        inputMethodManager.hideSoftInputFromWindow(windowToken, flag);
    }

}
