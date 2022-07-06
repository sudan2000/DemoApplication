package com.susu.baselibrary.speech.webapi;

import android.util.Base64;

import com.susu.baselibrary.speech.SpeechConstants;
import com.susu.baselibrary.utils.base.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import okhttp3.HttpUrl;

/**
 * Author : sudan
 * Time : 2021/11/1
 * Description:
 */
public class WebApiUtils {

    private static final String TAG = "test---";
    private static final String TYPE_FINAL = "0";
    private static final String TYPE_MIDDLE = "1";

    public static String getAuthUrl(String hostUrl, String apiKey, String apiSecret) {
        HttpUrl httpUrl = null;
        try {
            URL url = new URL(hostUrl);
            SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
            format.setTimeZone(TimeZone.getTimeZone("GMT"));
            String date = format.format(new Date());
            StringBuilder builder = new StringBuilder("host: ")
                    .append(url.getHost()).append("\n") //
                    .append("date: ").append(date).append("\n") //
                    .append("GET ").append(url.getPath()).append(" HTTP/1.1");

            //------------------
            Charset charset = Charset.forName("UTF-8");
            Mac mac = Mac.getInstance("hmacsha256");
            SecretKeySpec spec = new SecretKeySpec(apiSecret.getBytes(charset), "hmacsha256");
            mac.init(spec);
            byte[] hexDigits = mac.doFinal(builder.toString().getBytes(charset));
            String sha = getEncodeString(hexDigits);

            String authorization = String.format("api_key=\"%s\", algorithm=\"%s\", headers=\"%s\", signature=\"%s\"", apiKey, "hmac-sha256", "host date request-line", sha);
            httpUrl = HttpUrl.parse("https://" + url.getHost() + url.getPath()).newBuilder().
                    addQueryParameter("authorization", getEncodeString(authorization.getBytes(charset))).//
                    addQueryParameter("date", date).//
                    addQueryParameter("host", url.getHost()).//
                    build();
        } catch (Exception e) {
            LogUtils.d(TAG, e.getMessage());
        }

        if (httpUrl != null) {
            return httpUrl.toString();
        }
        LogUtils.e(TAG, "获取url失败");
        return "";
    }

    public static String getUrl(String appId, String apiSecret) {
        try {
            String url = SpeechConstants.BASE_URL + getHandShakeParams(appId, apiSecret);
            LogUtils.d(TAG, "url: " + url.toString());
            return url.toString();
        } catch (Exception e) {
            LogUtils.e(TAG, "获取url失败");
        }
        return null;
    }


    /**
     * 生成握手参数
     *
     * @param appId
     * @param secretKey
     * @return
     */
    public static String getHandShakeParams(String appId, String secretKey) {
        String ts = System.currentTimeMillis() / 1000 + "";
        String signa = "";
        //1.获取baseString，baseString由appId和当前时间戳ts拼接而成
        String baseString = appId + ts;
        //2.对baseString进行MD5
        String md5String = MD5(baseString);
        try {
            //3.以apiKey为key对MD5之后的baseString进行HmacSHA1加密，然后再对加密后的字符串进行base64编码。
            signa = HmacSHA1Encrypt(md5String, secretKey);
            return "?appid=" + appId + "&ts=" + ts + "&signa=" + URLEncoder.encode(signa, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }


    /**
     * 加密数字签名（基于HMACSHA1算法）
     */
    public static String HmacSHA1Encrypt(String encryptText, String encryptKey) throws SignatureException {
        byte[] rawHmac = null;
        try {
            byte[] data = encryptKey.getBytes("UTF-8");
            SecretKeySpec secretKey = new SecretKeySpec(data, "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(secretKey);
            byte[] text = encryptText.getBytes("UTF-8");
            rawHmac = mac.doFinal(text);
        } catch (InvalidKeyException e) {
            throw new SignatureException("InvalidKeyException:" + e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            throw new SignatureException("NoSuchAlgorithmException:" + e.getMessage());
        } catch (UnsupportedEncodingException e) {
            throw new SignatureException("UnsupportedEncodingException:" + e.getMessage());
        } catch (Exception e) {

        }
//        String oauth = new String(Base64.encodeBase64(rawHmac));
        String oauth = getEncodeString(rawHmac);

        return oauth;
    }

    private static String MD5(String pstr) {
        char md5String[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] btInput = pstr.getBytes();
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();

            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) { // i = 0
                byte byte0 = md[i]; // 95
                str[k++] = md5String[byte0 >>> 4 & 0xf]; // 5
                str[k++] = md5String[byte0 & 0xf]; // F
            }

            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }

    public static String getEncodeString(byte[] data) {
        //todo
//        return Base64.getEncoder().encodeToString(data);
        return new String(Base64.encode(data, Base64.DEFAULT)).replaceAll("\r", "").replaceAll("\n", "");
    }

    //----------------------------------处理返回数据----------------------------------

    /**
     * 实时语音数据处理
     */
    public static void handleResult(String msg, InnerWebApiListener mInnerWebApiListener) {
        try {
            JSONObject msgObj = new JSONObject(msg);
            String actionStr = msgObj.getString("action");
            if ("result".equals(actionStr)) {
                String data = msgObj.getString("data");
                getContent(data, mInnerWebApiListener);

            } else if ("error".equals(actionStr)) {
                String codeStr = msgObj.getString("code");
                LogUtils.d(TAG, "webSocket: action error code" + codeStr);

            } else if ("started".equals(actionStr)) {
                // 握手成功
                if (mInnerWebApiListener != null) {
                    mInnerWebApiListener.onOpen();
                }
            }
        } catch (JSONException e) {
            LogUtils.d(TAG, "解析数据报错：" + e.getMessage());
        }
    }


    private static void getContent(String data, InnerWebApiListener mInnerWebApiListener) {
        StringBuffer resultBuilder = new StringBuffer();
        try {
            JSONObject dataObj = new JSONObject(data);
            boolean isEnd = dataObj.getBoolean("ls"); //结束标志
            JSONObject cn = dataObj.getJSONObject("cn");
            JSONObject st = cn.getJSONObject("st");//st里面type判断

            JSONArray rtArr = st.getJSONArray("rt");
            for (int i = 0; i < rtArr.length(); i++) {
                JSONObject rtArrObj = rtArr.getJSONObject(i);
                JSONArray wsArr = rtArrObj.getJSONArray("ws");
                for (int j = 0; j < wsArr.length(); j++) {
                    JSONObject wsArrObj = wsArr.getJSONObject(j);
                    JSONArray cwArr = wsArrObj.getJSONArray("cw");
                    for (int k = 0; k < cwArr.length(); k++) {
                        JSONObject cwArrObj = cwArr.getJSONObject(k);
                        String wStr = cwArrObj.getString("w");
                        resultBuilder.append(wStr);
                    }
                }
            }

            String type = st.getString("type");//st里面type判断
            if (TYPE_FINAL.equals(type)) {
                LogUtils.d(TAG, "最终识别结果 ==》" + resultBuilder.toString());
                if (mInnerWebApiListener != null) {
                    mInnerWebApiListener.onFinalResult(resultBuilder.toString());
                    if (isEnd) {
                        mInnerWebApiListener.onClose();
                    }
                }

            } else {
//                LogUtils.d(TAG, "中间识别结果 ==》" + resultBuilder.toString());
//                if (mInnerWebApiListener != null) {
//                    mInnerWebApiListener.onMiddleResult(resultBuilder.toString());
//                }
            }

        } catch (Exception e) {
            LogUtils.d(TAG, "onMessage: 数据解析失败");
        }
    }


    /**
     * 流式听写webapi数据处理
     */
    private void getResult() {
//        mDecoder.discard();
//        WebApiResponseData resp = mGson.fromJson(text, WebApiResponseData.class);
//        if (resp != null) {
//            if (resp.getCode() != 0) {
//                LogUtils.d(TAG, "code=>" + resp.getCode() + " error=>" + resp.getMessage() + " sid=" + resp.getSid());
//                LogUtils.d(TAG, "错误码查询链接：https://www.xfyun.cn/document/error-code");
//                return;
//            }
//            if (resp.getData() != null) {
//                if (resp.getData().getResult() != null) {
//                    WebApiResponseData.Text te = resp.getData().getResult().getText();
//                    try {
//                        mDecoder.decode(te);
//                        LogUtils.d(TAG, "中间识别结果 ==》" + mDecoder.toString());
//                        if (mInnerWebApiListener != null) {
//                            mInnerWebApiListener.onMiddleResult(mDecoder.toString());
//                        }
//                    } catch (Exception e) {
//                        LogUtils.d(TAG, "onMessage: decodeError" + e.getMessage());
//                    }
//                }
//                if (resp.getData().getStatus() == 2) {
//                    // todo  resp.data.status ==2 说明数据全部返回完毕，可以关闭连接，释放资源
////                    if (mInnerWebApiListener != null) {
////                        mInnerWebApiListener.onFinalResult(mDecoder.toString());
////                    }
////                    LogUtils.d(TAG, "最终识别结果 ==》" + mDecoder.toString());
//////                    mDecoder.discard();
////                    webSocket.close(1000, "");
////                    if (mInnerWebApiListener != null) {
////                        mInnerWebApiListener.onClose();
////                    }
//                } else {
//                    // todo 根据返回的数据处理
//                }
//            }
//        }
    }

}
