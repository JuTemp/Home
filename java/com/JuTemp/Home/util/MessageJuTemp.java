package com.JuTemp.Home.util;

import android.os.*;

public final class MessageJuTemp {

    public static Message String2Message(Object content) {
        Message message = new Message();
        message.obj = content;
        return message;
    }

    public static Message String2Message(int what, Object content) {
        Message message = new Message();
        message.what = what;
        message.obj = content;
        return message;
    }

}
