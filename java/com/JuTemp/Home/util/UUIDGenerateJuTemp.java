package com.JuTemp.Home.util;

import java.util.*;

public class UUIDGenerateJuTemp {
    private static final String[] chars = new String[]{"a", "b", "c", "d", "e", "f",
            "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
            "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z"};

    public static String generateUUID(boolean bool) {
        if (bool) return UUID.randomUUID().toString();
        else return generateUUID(true).replace("-", "");
    }

    public static String generateShortUUID() {
        return generateShortUUID(8);
    }

    public static String generateShortUUID(int length) {
        StringBuilder shortBuffer = new StringBuilder();
        String uuid = generateUUID(false);

        for (int i = 0; i < 8; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            shortBuffer.append(chars[x % 0x3E]);
        }
        if (length<=8) return shortBuffer.substring(0,length);
        else return shortBuffer.append(generateShortUUID(length-8)).toString();
    }

}
