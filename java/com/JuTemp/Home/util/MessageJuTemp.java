package com.JuTemp.Home.util;

import android.os.*;

public final class MessageJuTemp {

    public static Message String2Message(Object context) {
        Message message = new Message();
        message.obj = context;
        return message;
    }

    public static Message String2Message(int what, Object context) {
        Message message = new Message();
        message.what = what;
        message.obj = context;
        return message;
    }

}
