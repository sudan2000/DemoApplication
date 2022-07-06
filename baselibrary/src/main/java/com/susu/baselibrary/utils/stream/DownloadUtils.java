package com.susu.baselibrary.utils.stream;

import com.susu.baselibrary.utils.base.LogUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Author : sudan
 * Time : 2021/6/12
 * Description:
 */
public class DownloadUtils {
    private static final String TAG = "DownloadUtils";

    private static final int BUFFER_SIZE = 10240;


    /**
     * @param filePath 不filePath 带有文件名
     */
    public static boolean downloadHttp(String filePath, String fileName, String urlString) {
        File file = FileUtils.prepareFile(filePath, fileName);
        return downloadHttp(file, urlString, BUFFER_SIZE);
    }


    /**
     * @param filePath 带有文件名
     */
    public static boolean downloadHttp(String filePath, String urlString) {
        File file = FileUtils.prepareFile(filePath);
        return downloadHttp(file, urlString, BUFFER_SIZE);
    }


    /**
     * http下载，返回是否下载成功；
     */
    public static boolean downloadHttp(File file, String urlString, int size) {
        InputStream is = null;
        OutputStream os = null;
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            is = connection.getInputStream();
            os = new FileOutputStream(file);
            byte[] buffer = new byte[size];
            int len = 0;
            while ((len = is.read(buffer)) > -1) {
                os.write(buffer, 0, len);
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            StreamUtils.streamClose(is, os);
        }
    }


//--------------------------->


    /**
     * 下载过程中在filePath目录下生成.temp文件， 作为临时文件下载；
     * 下载完成后进行用filePath进行重命名；
     *
     * @param filePath  带有文件名
     * @param urlString
     * @param listener  不能为空；
     * @return 下载是否成功
     */
    public static boolean downloadHttpByTemp(String filePath, String urlString, DownLoadListener listener) {
        InputStream is = null;
        OutputStream os = null;
        int count = 0;
        int length = 0;
        File tempFile = null;
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            is = connection.getInputStream();
            length = connection.getContentLength();
            if (length <= 0) {
                return false;
            }
            String tempPath = filePath + ".temp";
            tempFile = FileUtils.prepareFile(tempPath);
            os = new FileOutputStream(tempFile);

            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead = 0;
            while ((listener != null && !listener.canceled()) && (bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
                count = count + bytesRead;
                listener.progress(count, length);
            }
            if (listener != null && listener.canceled()) {
                if (tempFile != null) {
                    tempFile.delete();
                }
            } else {
                tempFile.renameTo(FileUtils.prepareFile(filePath));
                return true;
            }
            return false;

        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.i(TAG, "下载失败");
            if (tempFile != null) {
                tempFile.delete();
            }
            return false;
        } finally {
            StreamUtils.streamClose(is, os);
        }
    }


    public interface DownLoadListener {

        /**
         * return false，不取消下载
         */
        boolean canceled();

        /**
         * @param count  已下载
         * @param length 总size
         */
        void progress(int count, int length);

    }


//--------------------------->


    /**
     * 支持断点续传的下载
     * 如果咋在filePath目录下有.temp文件，则在这个基础上继续下载，否则创建.temp；
     * .temp作为临时文件下载，下载完成后进行用filePath进行重命名；
     *
     * @param filePath  带有文件名，建议文件名用urlString md5加密值拼接；
     * @param urlString
     * @param listener  不能为空；
     * @return 下载是否成功
     */
    public static boolean downloadHttpByPointContinue(String filePath, String urlString, PointContinueDownLoadListener listener) {
        InputStream is = null;
        RandomAccessFile rwd = null;
        int count = 0;
        int length = 0;
        File tempFile = null;
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            String tempPath = filePath + ".temp";
            tempFile = new File(tempPath);

            //获取下载文件length
            length = getContentLength(urlString);
            if (length <= 0) {
                setFailed(listener);
            }

            if (tempFile.exists()) {
                FileInputStream stream = new FileInputStream(tempFile);
                count = stream.available();
                // 设置下载位置(从服务器上取要下载文件的某一段)
                // 表示头500个字节：bytes=0-499
                // 表示第二个500字节：bytes=500-999
                // 表示500字节以后的范围：bytes=500-
                connection.setRequestProperty("Range", "bytes=" + count + "-");
            } else {
                tempFile = FileUtils.prepareFile(tempPath);
            }

            //设置tempFile从count位置开始保存
            rwd = new RandomAccessFile(tempFile, "rwd");
            rwd.seek(count);
            is = connection.getInputStream();

            int leaveLength = connection.getContentLength();
            LogUtils.i(TAG, "ResponseCode ==" + connection.getResponseCode() + "; 剩余要下载的leaveLength=" + leaveLength);

            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead = 0;
            while ((listener != null && !listener.canceled()) && (bytesRead = is.read(buffer)) != -1) {
                rwd.write(buffer, 0, bytesRead);
                count = count + bytesRead;
                LogUtils.i(TAG, "下载文件：count=" + count + "； length=" + length);
                listener.progress(count, length);
            }

            if (listener != null && !listener.canceled()) {
                LogUtils.i(TAG, "下载成功");
                tempFile.renameTo(FileUtils.prepareFile(filePath));
                listener.success();
                return true;
            } else {
                setFailed(listener);
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            setFailed(listener);
            return false;
        } finally {
            StreamUtils.streamClose(is, null);
            try {
                if (rwd != null) {
                    rwd.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private static void setFailed(PointContinueDownLoadListener listener) {
        LogUtils.i(TAG, "下载失败");
        if (listener != null) {
            listener.failed();
        }
    }

    /**
     * connection.setRequestProperty()得在getContentLength()之前调用，
     * 否则会报错，所以另起请求获取length；
     */
    private static int getContentLength(String urlString) {
        HttpURLConnection connection = null;
        int length = -1;
        try {
            //连接网络
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(3000);

            if (connection.getResponseCode() == 200) {//网络连接成功
                //获得文件长度
                length = connection.getContentLength();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return length;
    }


    public interface PointContinueDownLoadListener {

        /**
         * return false，不取消下载
         */
        boolean canceled();

        /**
         * @param count  已下载
         * @param length 总size
         */
        void progress(int count, int length);

        void success();

        void failed();
    }
}
