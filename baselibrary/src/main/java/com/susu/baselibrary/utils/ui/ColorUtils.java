package com.susu.baselibrary.utils.ui;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.TextUtils;


public class ColorUtils {
    private static final String COLOR_START = "#";

    public static int adjustAlpha(int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }

    private static final int ENABLE_ATTR = android.R.attr.state_enabled;
    private static final int CHECKED_ATTR = android.R.attr.state_checked;
    private static final int PRESSED_ATTR = android.R.attr.state_pressed;

    public static ColorStateList generateThumbColorWithTintColor(final int tintColor) {
        int[][] states = new int[][]{
                {-ENABLE_ATTR, CHECKED_ATTR},
                {-ENABLE_ATTR},
                {PRESSED_ATTR, -CHECKED_ATTR},
                {PRESSED_ATTR, CHECKED_ATTR},
                {CHECKED_ATTR},
                {-CHECKED_ATTR}
        };

        int[] colors = new int[]{
                tintColor - 0xAA000000,
                0xFFBABABA,
                tintColor - 0x99000000,
                tintColor - 0x99000000,
                tintColor | 0xFF000000,
                0xFFEEEEEE
        };
        return new ColorStateList(states, colors);
    }

    public static ColorStateList generateBackColorWithTintColor(final int tintColor) {
        int[][] states = new int[][]{
                {-ENABLE_ATTR, CHECKED_ATTR},
                {-ENABLE_ATTR},
                {CHECKED_ATTR, PRESSED_ATTR},
                {-CHECKED_ATTR, PRESSED_ATTR},
                {CHECKED_ATTR},
                {-CHECKED_ATTR}
        };

        int[] colors = new int[]{
                tintColor - 0xE1000000,
                0x10000000,
                tintColor - 0xD0000000,
                0x20000000,
                tintColor - 0xD0000000,
                0x20000000
        };
        return new ColorStateList(states, colors);
    }

    /**
     * ???????????? ?????????9??? alpha??????????????????
     *
     * @param colorString ??????
     * @return if not support -1 else color
     */
    public static int parseColorAlphaEndMode(String colorString) {
        return parseColorAlphaEndMode(colorString, -1);
    }

    /**
     * ???????????? ?????????9??? alpha??????????????????
     *
     * @param colorString
     * @param defaultColor ??????????????????????????????
     * @return
     */
    public static int parseColorAlphaEndMode(String colorString, int defaultColor) {
        if (TextUtils.isEmpty(colorString)) {
            return defaultColor;
        }

        if (!colorString.startsWith(COLOR_START)) {
            colorString = COLOR_START.concat(colorString);
        }
        StringBuilder sb = new StringBuilder();
        if (colorString.length() == 4) {
            String r = colorString.substring(1, 2);
            String g = colorString.substring(2, 3);
            String b = colorString.substring(3, 4);
            sb.append(COLOR_START)
                    .append(r)
                    .append(r)
                    .append(g)
                    .append(g)
                    .append(b)
                    .append(b);
        } else if (colorString.length() == 7) {
            sb.append(colorString);
        } else if (colorString.length() == 9) {
            // ?????????#rrggbbaa???????????????
            String rgb = colorString.substring(1, 7);
            String a = colorString.substring(7, 9);
            sb.append(COLOR_START)
                    .append(a)
                    .append(rgb);
        }
        if (sb.length() < 1) {
            return defaultColor;
        }
        try {
            return Color.parseColor(sb.toString());
        } catch (IllegalArgumentException exp) {
            exp.printStackTrace();
        }

        return defaultColor;
    }

    public static int getColor(Context context, int resId) {
        return context.getResources().getColor(resId);
    }

    /**
     * ?????????startColor?????????endColor?????????????????????franch???????????????
     *
     * @param startColor ???????????? int??????
     * @param endColor   ???????????? int??????
     * @param franch     franch ?????????0.5
     * @return ??????int?????????color
     */
    public static int caculateColor(int startColor, int endColor, float franch) {
        String strStartColor = "#" + Integer.toHexString(startColor);
        String strEndColor = "#" + Integer.toHexString(endColor);
        return Color.parseColor(caculateColor(strStartColor, strEndColor, franch));
    }

    /**
     * ?????????startColor?????????endColor?????????????????????franch???????????????
     *
     * @param startColor ???????????? ?????????#FFFFFFFF???
     * @param endColor   ???????????? ?????????#FFFFFFFF???
     * @param franch     ?????????0.5
     * @return ??????String?????????color?????????#FFFFFFFF???
     */
    public static String caculateColor(String startColor, String endColor, float franch) {

        int startAlpha = Integer.parseInt(startColor.substring(1, 3), 16);
        int startRed = Integer.parseInt(startColor.substring(3, 5), 16);
        int startGreen = Integer.parseInt(startColor.substring(5, 7), 16);
        int startBlue = Integer.parseInt(startColor.substring(7), 16);

        int endAlpha = Integer.parseInt(endColor.substring(1, 3), 16);
        int endRed = Integer.parseInt(endColor.substring(3, 5), 16);
        int endGreen = Integer.parseInt(endColor.substring(5, 7), 16);
        int endBlue = Integer.parseInt(endColor.substring(7), 16);

        int currentAlpha = (int) ((endAlpha - startAlpha) * franch + startAlpha);
        int currentRed = (int) ((endRed - startRed) * franch + startRed);
        int currentGreen = (int) ((endGreen - startGreen) * franch + startGreen);
        int currentBlue = (int) ((endBlue - startBlue) * franch + startBlue);

        return "#" + getHexString(currentAlpha) + getHexString(currentRed)
                + getHexString(currentGreen) + getHexString(currentBlue);

    }

    /**
     * ???10????????????????????????16?????????
     */
    public static String getHexString(int value) {
        String hexString = Integer.toHexString(value);
        if (hexString.length() == 1) {
            hexString = "0" + hexString;
        }
        return hexString;
    }
}
