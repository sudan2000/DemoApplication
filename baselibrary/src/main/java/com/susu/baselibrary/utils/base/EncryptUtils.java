package com.susu.baselibrary.utils.base;

import com.susu.baselibrary.utils.stream.StreamUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Author : sudan
 * Time : 2021/6/12
 * Description:
 */
public class EncryptUtils {

    private static final char HEX_DIGITS[] =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    /**
     * 根据filePath，将File内容转成MD5值
     */
    public static String encryptMD5File2String(final String filePath) {
        File file = isSpace(filePath) ? null : new File(filePath);
        return encryptMD5File2String(file);
    }

    /**
     * Return the bytes of file's MD5 encryption.
     *
     * @param filePath The path of file.
     * @return the bytes of file's MD5 encryption
     */
    public static byte[] encryptMD5File2Byte(final String filePath) {
        File file = isSpace(filePath) ? null : new File(filePath);
        return encryptMD5File(file);
    }

    /**
     * Return the hex string of file's MD5 encryption.
     * 将File内容转成MD5值
     *
     * @param file The file.
     * @return the hex string of file's MD5 encryption
     */
    public static String encryptMD5File2String(final File file) {
        return bytes2HexString(encryptMD5File(file));
    }


    /**
     * 对InputStream进行MD5加密，得到byte
     */
    public static byte[] encryptMD5File(final File file) {
        if (file == null) return null;
        FileInputStream fis = null;
        DigestInputStream digestInputStream;
        try {
            fis = new FileInputStream(file);
            MessageDigest md = MessageDigest.getInstance("MD5");
            digestInputStream = new DigestInputStream(fis, md);
            byte[] buffer = new byte[256 * 1024];
            while (true) {
                if (!(digestInputStream.read(buffer) > 0)) break;
            }
            md = digestInputStream.getMessageDigest();
            return md.digest();
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            StreamUtils.streamClose(fis, null);
        }
    }

    /**
     * 将bytes转成16进制的字符串
     */
    public static String bytes2HexString(final byte[] bytes) {
        if (bytes == null) return "";
        int len = bytes.length;
        if (len <= 0) return "";
        char[] ret = new char[len << 1];
        for (int i = 0, j = 0; i < len; i++) {
            ret[j++] = HEX_DIGITS[bytes[i] >> 4 & 0x0f];
            ret[j++] = HEX_DIGITS[bytes[i] & 0x0f];
        }
        return new String(ret);
    }

    private static boolean isSpace(final String s) {
        if (s == null) return true;
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }


    /**
     * 将字符串转成MD5值
     */
    public static String stringToMD5(String string) {
        try {
            byte[] hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
            StringBuilder hex = new StringBuilder(hash.length * 2);
            for (byte b : hash) {
                if ((b & 0xFF) < 0x10) {
                    hex.append("0");
                }
                hex.append(Integer.toHexString(b & 0xFF));
            }
            return hex.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 对 filePath 进行md5完整性校验
     *
     * @param filePath 被校验的文件路径
     * @param md5      被比对的md5值
     */
    public static boolean MD5FilePathCheck(String filePath, String md5) {
        if (!StringUtils.isEmpty(filePath)) {
            return MD5FileCheck(new File(filePath), md5);
        } else {
            return false;
        }
    }

    /**
     * 对 file 进行完整性校验
     */
    public static boolean MD5FileCheck(File file, String md5) {
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            return MD5StreamCheck(fileInputStream, md5);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 对InputStream进行md5完整性校验
     *
     * @param inputStream 被校验的文件流
     * @param md5         被比对的md5值
     */
    public static boolean MD5StreamCheck(InputStream inputStream, String md5) {
        if (inputStream == null || StringUtils.isEmpty(md5)) {
            return false;
        }
        MessageDigest digest;
        byte buffer[] = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");

            while ((len = inputStream.read(buffer)) != -1) {
                digest.update(buffer, 0, len);
            }
            BigInteger bigInt = new BigInteger(1, digest.digest());
            String fileMd5 = bigInt.toString(16);
            return md5.equals(fileMd5);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            StreamUtils.streamClose(inputStream, null);
        }
        return false;
    }


    /**
     * SHA1完整性校验
     */
    public static boolean SHA1StrVerifier(String archives, String SHA1) {
        if (StringUtils.isNull(archives) || StringUtils.isNull(SHA1)) {
            return false;
        }

        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
            InputStream data = new FileInputStream(new File(archives));
            byte[] input = new byte[2046];
            int num;
            while ((num = data.read(input)) > 0) {
                messageDigest.update(input, 0, num);
            }
            String manifest = encode2HexString(messageDigest.digest());
            return SHA1.equals(manifest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String encode2HexString(byte[] data) {
        StringBuilder hex = new StringBuilder(data.length * 2);
        for (byte b : data) {
            if ((b & 0xFF) < 0x10) {
                hex.append("0");
            }
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString().toLowerCase();
    }

}
