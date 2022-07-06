package com.susu.baselibrary.utils.fileProvider;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import com.susu.baselibrary.utils.system.CoreUtils;

import java.io.File;

/**
 * Author : sudan
 * Time : 2021/7/27
 * Description: Android 7.0 FileProvider使用
 */
public class FileProviderUtils {
    private final static String AUTHORITIES = CoreUtils.getApp().getPackageName() + ".susu.fileprovider";


    public static Uri getFileUri(Context context, File file) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return SuFileProvider.getUriForFile(context, AUTHORITIES, file);
        }
        return Uri.fromFile(file);
    }

    public static Intent setIntentDataAndType(Intent intent, String type, File file, boolean writable) {
        Context context = CoreUtils.getApp();
        return setIntentDataAndType(context, intent, type, file, writable);
    }


    public static Intent setIntentDataAndType(Context context,
                                              Intent intent,
                                              String type,
                                              File file) {
        return setIntentDataAndType(context, intent, type, file, false);
    }

    private static Intent setIntentDataAndType(Context context, Intent intent, String type, File file, boolean writable){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setDataAndType(SuFileProvider.getUriForFile(context, AUTHORITIES, file), type);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            if (writable) {
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
        } else {
            intent.setDataAndType(Uri.fromFile(file), type);
        }
        return intent;
    }
}
