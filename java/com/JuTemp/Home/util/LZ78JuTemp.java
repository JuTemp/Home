package com.JuTemp.Home.util;

import java.util.ArrayList;
import java.util.Objects;

public class LZ78JuTemp {

    static final int OFFSET = 33;

    public static String encode(String strLong) {
        StringBuilder strShortBuilder = new StringBuilder();
        ArrayList<String> lz78Dictionary = new ArrayList<>();
        StringBuilder prefix = new StringBuilder();
        char[] charArrayLong = strLong.toCharArray();

        o:
        for (char c : charArrayLong) {
            for (String w : lz78Dictionary) {
                if (Objects.equals(prefix.toString() + c, w)) {
                    prefix.append(c);
                    continue o;
                }
            }
            int i=ifLargerThan126(lz78Dictionary.indexOf(prefix.toString()) + 1 + OFFSET);
            if (i==-1) return null;
            strShortBuilder.append((char) i).append(c);
            lz78Dictionary.add(prefix.toString() + c);
            prefix.setLength(0);
        }
        if (prefix.length() != 0) {
            int i = ifLargerThan126(lz78Dictionary.indexOf(prefix.toString()) + 1 + OFFSET);
            if (i == -1) return null;
            strShortBuilder.append((char) i);
        }
        return strShortBuilder.toString();
    }

    private static int ifLargerThan126(int result) {
        if (result > 126) return -1;
        else return result;
    }

    public static String decode(String strShort) {
        StringBuilder strLongBuilder = new StringBuilder();
        ArrayList<String> lz78Dictionary = new ArrayList<>();
        char[] charArrayLong = strShort.toCharArray();

        try {
            int i = 0;
            while (i < strShort.length()) {
                int w = charArrayLong[i++] - OFFSET/*-48*/;
                char c;
                if (i < strShort.length()) c = charArrayLong[i++];
                else {
                    strLongBuilder.append(lz78Dictionary.get(w - 1));
                    break;
                }
                if (w == 0) {
                    strLongBuilder.append(c);
                    lz78Dictionary.add(String.valueOf(c));
                } else {
                    strLongBuilder.append(lz78Dictionary.get(w - 1)).append(c);
                    lz78Dictionary.add(lz78Dictionary.get(w - 1) + c);
                }
            }
            return strLongBuilder.toString();
        } catch (Exception e) {
            return null;
        }
    }
}
