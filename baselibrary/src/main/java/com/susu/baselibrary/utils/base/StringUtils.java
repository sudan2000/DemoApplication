package com.susu.baselibrary.utils.base;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;

import java.util.Locale;

/**
 * Author : sudan
 * Time : 2020/12/11
 * Description:
 */

public class StringUtils {

    public static String replaceStrWithNewLine(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        return str.replaceAll("[\\t\\n\\r]", "\r\n");
    }

    public static String replaceStrWithBlank(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        return str.replaceAll("\\r\\n|\\r|\\n", " ");
    }

    private final static char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0 || str.trim().length() == 0;
    }

    public static boolean isNull(String str) {
        return str == null || str.length() == 0 || str.trim().length() == 0;
    }

    public static boolean isNotNull(String str) {
        return str != null && str.length() != 0 && str.trim().length() != 0;
    }


    public static SpannableString changeTextSize(String str, int size, int startIndex, int endIndex) {
        SpannableString spannableString = new SpannableString(str);

        spannableString.setSpan(new AbsoluteSizeSpan(size), startIndex, endIndex, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    /**
     * 超过 1 w 展示 1.xW
     *
     * @param number 数字
     * @return
     */
    public static String getShowNumber(long number) {
        if (number > 10000) {
            int wN = (int) (number / 10000);
            int tN = ((int) (number / 1000)) % 10;
            if (tN == 0) {
                return String.format(Locale.CHINA, "%dw", wN);
            }
            return String.format(Locale.CHINA, "%d.%dw", wN, tN);
        }

        return String.format(Locale.CHINA, "%d", number);
    }
}
