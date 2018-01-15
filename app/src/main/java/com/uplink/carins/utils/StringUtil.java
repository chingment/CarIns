package com.uplink.carins.utils;

/**
 * Created by chingment on 2017/12/18.
 */

public class StringUtil {

    public static boolean isEmpty(Object str) {
        return str == null || str.toString().length() == 0;
    }

    public static boolean isEmptyNotNull(String input) {
        if (input == null || "".equals(input) || "null".equals(input.toLowerCase()))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }
}
