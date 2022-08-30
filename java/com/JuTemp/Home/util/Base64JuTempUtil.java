package com.JuTemp.Home.util;

import java.util.Arrays;
import java.util.Base64;

public class Base64JuTempUtil {

    public static String encode(String strLong) {
        return Base64.getEncoder().encodeToString(strLong.getBytes());
    }

    public static String decode(String strShort) {
        try {
            return new String(Base64.getDecoder().decode(strShort));
        } catch (Exception e) {
            return null;
        }
    }
}
