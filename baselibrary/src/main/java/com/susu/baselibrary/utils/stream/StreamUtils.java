package com.susu.baselibrary.utils.stream;

import com.baidubce.model.User;
import com.susu.baselibrary.utils.base.LogUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.StringReader;

/**
 * Author : sudan
 * Time : 2021/6/12
 * Description:
 * 1.File、Stream、String、byte互转
 * 2.关闭流
 */
public class StreamUtils {

    private static final String TAG = "StreamUtils";

    /**
     * inputStream 转 ByteArrayOutputStream
     * 一个inputStream只能read一次，ByteArrayOutputStream可以用于新开一个inputStream流；
     */
    public static ByteArrayOutputStream cloneInputStream(InputStream inputStream) {
        if (inputStream == null) {
            return null;
        }
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) >= 0) {
                baos.write(buffer, 0, len);
            }
            baos.flush();
            return baos;
        } catch (IOException e) {
            LogUtils.d(TAG, e.getMessage());
        }
        return null;
    }

    /**
     * 将Stream从 is copy到 os 中
     */
    public static void copyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        try {
            byte[] bytes = new byte[buffer_size];
            for (; ; ) {
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1) {
                    break;
                }
                os.write(bytes, 0, count);
            }
        } catch (Exception e) {
            LogUtils.d(TAG, e.getMessage());
        }
    }


    /**
     * InputStream 转 String
     */
    public static String streamToString(InputStream inputStream) {
        if (inputStream == null) {
            return "";
        }
        try {
            ByteArrayOutputStream result = cloneInputStream(inputStream);
            return result.toString("utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            streamClose(inputStream, null);
        }

        return "";
    }

    /**
     * InputStream 写入 File
     */
    public static void streamToFile(InputStream inputStream, File targetFile) {
        if (inputStream == null) {
            return;
        }

        if (!targetFile.getParentFile().exists()) {
            targetFile.getParentFile().mkdirs();
        }
        OutputStream outStream = null;
        try {
            outStream = new FileOutputStream(targetFile);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            streamClose(inputStream, outStream);
        }
    }

    /**
     * string to file
     */
    public static boolean stringToFile(String str, String filePath) {
        boolean flag = true;
        File distFile = new File(filePath);
        if (!distFile.getParentFile().exists()) {
            distFile.getParentFile().mkdirs();
        }
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;
        try {
            bufferedReader = new BufferedReader(new StringReader(str));
            bufferedWriter = new BufferedWriter(new FileWriter(distFile));
            char buf[] = new char[1024]; // 字符缓冲区
            int len;
            while ((len = bufferedReader.read(buf)) != -1) {
                bufferedWriter.write(buf, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
            flag = false;
        } finally {
            streamClose(bufferedReader, bufferedWriter);
        }
        return flag;
    }

    /**
     * 将文件读出转化String
     *
     * @param filePath 文件路径
     */
    public static String readFile(String filePath) {
        String result = "";
        try {
            File file = new File(filePath);
            if (file.isFile() && file.exists()) {
                InputStreamReader read = new InputStreamReader(new FileInputStream(file), "utf-8");
                BufferedReader bufferedReader = new BufferedReader(read);
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                read.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 下载结束，finally 中 InputStream、OutputStream 的 close()、flush()操作
     */
    public static void streamClose(InputStream is, OutputStream os) {
        try {
            if (null != is) {
                is.close();
            }
            if (null != os) {
                os.flush();
                os.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * finally 中 BufferedReader、BufferedWriter 的 close()、flush()操作
     */
    public static void streamClose(BufferedReader reader, BufferedWriter writer) {
        try {
            if (null != reader) {
                reader.close();
            }
            if (null != writer) {
                writer.flush();
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeObject(String path, Object object) {
        ObjectOutputStream os = null;
        try {
            os = new ObjectOutputStream(new FileOutputStream(path));
            os.writeObject(object);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != os) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public static Object readObject(String path) {
        ObjectInputStream is = null;
        try {
            is = new ObjectInputStream(new FileInputStream("./user.txt"));
            return is.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
