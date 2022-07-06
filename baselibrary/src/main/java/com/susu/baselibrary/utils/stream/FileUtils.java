package com.susu.baselibrary.utils.stream;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.susu.baselibrary.speech.SpeechConstants;
import com.susu.baselibrary.utils.base.LogUtils;
import com.susu.baselibrary.utils.base.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Author : sudan
 * Time : 2021/6/12
 * Description:
 */
public class FileUtils {

    private static final String TAG = "FileUtils";

    public static final String PATH_FOLDER = Environment
            .getExternalStorageDirectory().getPath();

    public static final String PATH_IMAGE_SOURCE = Environment
            .getExternalStorageDirectory() + "/%1$s/image/source";


    /**
     * 判断sd卡是否存在
     */
    public static boolean existSDCard() {
        return Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED);
    }


    /**
     * 根据path，判断文件是否存在
     */
    public static boolean fileIsExists(String filePath) {
        if (StringUtils.isEmpty(filePath)) {
            return false;
        }

        File tempFile = new File(filePath);
        return tempFile.exists();
    }

    /**
     * 获取文件size大小
     */
    public static long getDirSize(File file) {
        if (!file.exists()) {
            return 0;
        }
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (File value : fileList) {
                if (value.isDirectory()) {
                    size = size + getDirSize(value);

                } else {
                    size = size + value.length();

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }


    /**
     * 根据filePath、fileName 生成File， 如果文件存在则删除，重新创建
     *
     * @param fileName 文件名
     */
    public static File prepareFile(String filePath, String fileName) {
        File dir = new File(filePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File tempFile = new File(dir, fileName);
        if (tempFile.exists()) {
            tempFile.delete();
        }
        try {
            tempFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tempFile;
    }


    /**
     * 根据filePath 生成File
     *
     * @param filePath 带文件名的路径 .../susu/test.txt
     */
    public static File prepareFile(String filePath) {
        File tempFile = new File(filePath);
        if (tempFile.exists()) {
            tempFile.delete();
        } else {
            tempFile.getParentFile().mkdirs();
        }
        try {
            tempFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tempFile;
    }


    public static File getDir(String format, Object... strs) {
        String path = String.format(format, strs);
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        return dir;
    }

    public static String getPath(String format, Object... strs) {
        return String.format(format, strs);
    }

    /**
     * 如果是文件：直接删除；如果是文件夹，则删除文件夹下面的文件和文件夹
     *
     * @param oldFile 文件夹的路径地址
     * @param bool    true：则删除当前文件夹空目录；false：则不删除
     */
    public static void deleteDir(File oldFile, boolean bool) {
        if (oldFile == null || !oldFile.exists()) {
            return;
        }
        if (oldFile.isDirectory()) {
            File[] files = oldFile.listFiles();
            for (File file : files) {
                deleteDir(file, true);
            }
            if (bool) {
                oldFile.delete();
            }
        } else {
            oldFile.delete();
        }
    }

    /**
     * 删除文件夹和子文件夹里面的文件
     */
    public static boolean deleteDir(final String path) {
        boolean result = false;
        if (!StringUtils.isEmpty(path)) {
            File dir = new File(path);
            result = deleteDirWithFile(dir);
        }
        return result;
    }

    private static boolean deleteDirWithFile(File dir) {
        if (dir == null || !dir.exists() || !dir.isDirectory()) {
            return false;
        }
        for (File file : dir.listFiles()) {
            if (file.isFile()) {
                file.delete(); // 删除所有文件
            } else {
                if (file.isDirectory()) {
                    deleteDirWithFile(file); // 递归的方式删除文件夹}}
                }
            }
        }
        dir.delete();// 删除目录本身
        return true;
    }


    public static boolean copyFile(File src, File des) {
        if (des.exists()) {
            des.delete();
        }

        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(src);
            os = new FileOutputStream(des);
            StreamUtils.copyStream(is, os);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            StreamUtils.streamClose(is, os);
        }
        return false;
    }

    public static Uri parseUri(@NonNull String path) {
        Uri uri;
        if (TextUtils.isEmpty(path)) {
            return null;
            // See https://pmd.github.io/pmd-6.0.0/pmd_rules_java_performance.html#simplifystartswith
        } else if (path.charAt(0) == '/') {
            uri = toFileUri(path);
        } else {
            uri = Uri.parse(path);
            String scheme = uri.getScheme();
            if (scheme == null) {
                uri = toFileUri(path);
            }
        }
        return uri;
    }

    private static Uri toFileUri(String path) {
        return Uri.fromFile(new File(path));
    }

    public static String copyAssetsFile(Context context, String fileName) {
        Context finalContext = context.getApplicationContext();
        final File cacheFile = new File(SpeechConstants.FOLDER, fileName);
        InputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {
            inputStream = finalContext.getAssets().open(fileName);
            outputStream = new FileOutputStream(cacheFile);
            byte[] buf = new byte[1024];
            int len;
            while ((len = inputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, len);
            }
            LogUtils.d(TAG, "fileName:" + cacheFile.getAbsolutePath());
            return cacheFile.getAbsolutePath();
        } catch (IOException e) {
            LogUtils.d(TAG, "copyAssetsFile error:" + e.getMessage());

        } finally {
            StreamUtils.streamClose(inputStream, outputStream);
        }
        return "";
    }

}
