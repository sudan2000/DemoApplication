package com.susu.baselibrary.sharelogin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Author : sudan
 * Time : 2022/1/13
 * Description:
 */
public class ShareLoginManager {
    private static WeakReference<Activity> sActivityWeak;

    public static void setAppId(String weixinAppId, String weixinSecret,
                                String weiboAppId, String QQAppId) {
        ShareLoginConstants.WEIXIN_APPID = weixinAppId;
        ShareLoginConstants.WEIXIN_SECRET = weixinSecret;
        ShareLoginConstants.WEIBO_APPID = weiboAppId;
        ShareLoginConstants.QQ_APPID = QQAppId;
    }

    public static void setWeiXinAppId(String appId, String secret) {
        ShareLoginConstants.WEIXIN_APPID = appId;
        ShareLoginConstants.WEIXIN_SECRET = secret;
    }

    public static void setWeiBoAppId(String appId, String redirectUrl, String scope) {
        ShareLoginConstants.WEIBO_APPID = appId;
        ShareLoginConstants.WEIBO_REDIRECT_URL = redirectUrl;
        ShareLoginConstants.WEIBO_SCOPE = scope;
    }

    public static void setQQAppId(String appId, String scope) {
        ShareLoginConstants.QQ_APPID = appId;
        ShareLoginConstants.QQ_SCOPE = scope;
    }

    /**
     * 由于H5页面单独进程，需要提前在主进程里初始化（主要是微博分享)
     * 该方法需要在setAppId（setWeiXinAppId、setWeiBoAppId、setQQAppId）之后调用
     */
    public static void init(Context context) {
        Context applicationContext = context.getApplicationContext();
//        WbSdk.install(applicationContext, new AuthInfo(applicationContext, ShareLoginConstants.WEIBO_APPID,
//                ShareLoginConstants.WEIBO_REDIRECT_URL, ShareLoginConstants.WEIBO_SCOPE));
//        getThirdPartnerConfig(context);
    }

    /**
     * 从服务端获取三方config
     * 取clientId作为微信的appId或QQ的appKey
     */
    private static void getThirdPartnerConfig(final Context context) {
//        SimplifyJSONListener<ShareLoginGetPartnerConfigResponse> mQQListener = new SimplifyJSONListener<ShareLoginGetPartnerConfigResponse>() {
//            @Override
//            public void onSuccess(ShareLoginGetPartnerConfigResponse response) {
//                saveThirdPartnerConfig(context, ShareLoginConstants.CLIENT_ID_QQ, response.clientId);
//            }
//
//            @Override
//            public void onFailed(Throwable throwable) {
//
//            }
//        };
//        new ShareLoginGetPartnerConfigRequest(ShareLoginGetPartnerConfigRequest.ACCESS_DOMAIN_QQ).schedule(mQQListener);
//
//        SimplifyJSONListener<ShareLoginGetPartnerConfigResponse> mWeixinListener = new SimplifyJSONListener<ShareLoginGetPartnerConfigResponse>() {
//            @Override
//            public void onSuccess(ShareLoginGetPartnerConfigResponse response) {
//                saveThirdPartnerConfig(context, ShareLoginConstants.CLIENT_ID_WEIXIN, response.clientId);
//            }
//
//            @Override
//            public void onFailed(Throwable throwable) {
//
//            }
//        };
//        new ShareLoginGetPartnerConfigRequest(ShareLoginGetPartnerConfigRequest.ACCESS_DOMAIN_WEIXIN).schedule(mWeixinListener);

    }

    private static void saveThirdPartnerConfig(Context context, String key, String value) {
        context.getApplicationContext().getSharedPreferences("THIRD_LOGIN_PARTNER_CONFIG", 0).edit().putString(key, value).apply();
    }

    public static String getThirdPartnerConfig(Context context, String key, String defValue) {
        return context.getApplicationContext().getSharedPreferences("THIRD_LOGIN_PARTNER_CONFIG", 0).getString(key, defValue);
    }

    /**
     * 分享到QQ空间
     * 注意：需要在Activity的onActivityResult方法里调用本类的onActivityResult，listener才会有回调
     *
     * @param title     必填。分享的标题，最多200个字符。超过后将被截断
     * @param targetUrl 必填。需要跳转的链接，URL字符串。
     * @param summary   选填。分享的摘要，最多600字符。超过后将被截断
     * @param imageUrls 选填。分享的图片, 以ArrayList<String>的类型传入，以便支持多张图片
     *                  （注：图片最多支持9张图片，多余的图片会被丢弃）。<br/>
     *                  注意:<br/>
     *                  (1)官网提示：QZone接口暂不支持发送多张图片的能力，若传入多张图片，
     *                  则会自动选入第一张图片作为预览图。多图的能力将会在以后支持。<br/>
     *                  (2)list中URL类型要一致，要么都是网络图片，要是都是本地图片
     * @param listener  可选。分享回调
     */
    public static void shareToQQzone(Activity activity, String title,
                                     String targetUrl, String summary,
                                     ArrayList<String> imageUrls, ShareListener listener) {
//        recordActivity(activity);
//        try {
//            boolean result = new ShareQQManager(activity)
//                    .shareToQzone(title, targetUrl, summary, imageUrls, listener);
//            if (!result && listener != null) {
//                listener.onCancel();
//            }
//        } catch (Throwable e) {
//            if (listener != null) {
//                listener.onException(e);
//            }
//        }
    }

    /**
     * 发表说说或上传图片
     * 注意：需要在Activity的onActivityResult方法里调用ShareQQManager.onActivityResult，listener才会有回调
     *
     * @param summary   选填：说说正文（传图和传视频接口会过滤第三方传过来的自带描述，目的为了鼓励用户自行输入有价值信息）
     * @param imageUrls 选填：说说的图片, 以ArrayList<String>的类型传入，以便支持多张图片
     *                  （注：<=9张图片为发表说说，>9张为上传图片到相册），只支持本地图片。
     * @param listener  选填：分享回调
     */
    public static void publishMoodToQzone(Activity activity, String summary,
                                          ArrayList<String> imageUrls, ShareListener listener) {
//        recordActivity(activity);
//        try {
//            boolean result = new ShareQQManager(activity).publishMoodToQzone(summary, imageUrls, listener);
//            if (!result && listener != null) {
//                listener.onCancel();
//            }
//        } catch (Throwable e) {
//            if (listener != null) {
//                listener.onException(e);
//            }
//        }
    }

    /**
     * 分享给QQ好友
     * 注意：需要在Activity的onActivityResult方法里调用本类的onActivityResult，listener才会有回调
     *
     * @param title     必填。分享的标题, 最长30个字符。超过后将被截断
     * @param targetUrl 必填。这条分享消息被好友点击后的跳转URL。
     * @param summary   可选。分享的消息摘要，最长40个字。超过后将被截断
     * @param imageUrl  可选。分享图片的URL或者本地路径
     * @param listener  可选。分享回调
     */
    public static void shareToQQFriend(Activity activity, String title,
                                       String targetUrl, String summary,
                                       String imageUrl, ShareListener listener) {
//        recordActivity(activity);
//        try {
//            boolean result = new ShareQQManager(activity).shareToFriend(title, targetUrl,
//                    summary, imageUrl, listener);
//            if (!result && listener != null) {
//                listener.onCancel();
//            }
//        } catch (Throwable e) {
//            if (listener != null) {
//                listener.onException(e);
//            }
//        }
    }

    /**
     * 分享纯图片给QQ好友
     * 注意：需要在Activity的onActivityResult方法里调用ShareQQManager.onActivityResult，listener才会有回调
     *
     * @param imageLocalUrl 必填：需要分享的本地图片路径
     * @param listener      可选：分享回调
     */
    public static void shareImageToQQFriend(Activity activity, String imageLocalUrl, ShareListener listener) {
//        recordActivity(activity);
//        try {
//            boolean result = new ShareQQManager(activity).shareImageToFriend(imageLocalUrl, listener);
//            if (!result && listener != null) {
//                listener.onCancel();
//            }
//        } catch (Throwable e) {
//            if (listener != null) {
//                listener.onException(e);
//            }
//        }
    }

    /**
     * 调用 QQ 登录。
     * 注：需要在Activity的onActivityResult回调里调用本类的onActivityResult方法，listener才会有回调。
     */
    public static void loginByQQ(Activity activity, LoginListener listener) {
//        recordActivity(activity);
//        try {
//            boolean result = new LoginQQManager(activity).login(listener);
//            if (!result && listener != null) {
//                listener.onCancel();
//            }
//        } catch (Throwable e) {
//            if (listener != null) {
//                listener.onException(e);
//            }
//        }
    }

    /**
     * 分享给微信朋友<br/>
     * 注：需要在WXEntryActivity的onCreate,onNewIntent里调用本类的onWeixinResultData方法，listener才会响应
     *
     * @param shareContentType 分享类型，0-普通分享；1-纯图片分享
     * @param title            标题 可空（不能与描述同时为空）。长度不超过512，超出将被截断
     * @param description      描述 可空（不能与标题同时为空）。长度不超过1024，超出将被截断
     * @param thumbData        图片 可空。长度不超过32768(32k)，超出将直接return false；
     * @param imageData        图片 可空。内容大小不超过 10MB（建议用imagePath，超过512K可能受限制）
     * @param imagePath        图片地址 可空。
     * @param url              链接 可空。长度不能超过10240，超出将直接return false；
     * @param listener         可空。回调监听器
     */
    public static void shareToWeixinFriend(Activity activity, int shareContentType, String title,
                                           String description, byte[] thumbData, byte[] imageData,
                                           String imagePath, String url, ShareListener listener) {
//        recordActivity(activity);
//        try {
//            boolean result = new ShareWeixinManager(activity)
//                    .shareToFriend(shareContentType, title, description,
//                            thumbData, imageData, imagePath, url, listener);
//            if (!result && listener != null) {
//                listener.onCancel();
//            }
//        } catch (Throwable e) {
//            if (listener != null) {
//                listener.onException(e);
//            }
//        }
    }

    /**
     * 分享给微信朋友<br/>
     * 注：需要在WXEntryActivity的onCreate,onNewIntent里调用本类的onWeixinResultData方法，listener才会响应
     *
     * @param title       标题 可空（不能与描述同时为空）。长度不超过512，超出将被截断
     * @param description 描述 可空（不能与标题同时为空）。长度不超过1024，超出将被截断
     * @param thumbData   图片 可空。长度不超过32768(32k)，超出将直接return false；
     * @param url         链接 可空。长度不能超过10240，超出将直接return false；
     * @param listener    可空。回调监听器
     */
    public static void shareToWeixinFriend(Activity activity, String title,
                                           String description, byte[] thumbData,
                                           String url, ShareListener listener) {
        shareToWeixinFriend(activity, 0, title, description, thumbData, null, null, url, listener);
    }

    /**
     * 分享到朋友圈<br/>
     * 注：需要在WXEntryActivity的onCreate,onNewIntent里调用本类的onWeixinResultData方法，listener才会响应
     *
     * @param shareContentType 分享类型，0-普通分享；1-纯图片分享
     * @param title            标题 可空。长度不超过512，超出将被截断
     * @param description      描述 可空。长度不超过1024，超出将被截断
     * @param thumbData        图片 可空。长度不超过32768(32k)，超出将直接return false；
     * @param imageData        图片 可空。内容大小不超过 10MB（建议用imagePath，超过512K可能受限制）
     * @param imagePath        图片地址 可空。
     * @param url              链接 可空。长度不能超过10240，超出将直接return false；
     * @param listener         可空。回调监听器
     */
    public static void shareToWeixinTimeline(Activity activity, int shareContentType, String title,
                                             String description, byte[] thumbData, byte[] imageData, String imagePath,
                                             String url, ShareListener listener) {
//        recordActivity(activity);
//        try {
//            boolean result = new ShareWeixinManager(activity)
//                    .shareToTimeline(shareContentType, title, description,
//                            thumbData, imageData, imagePath, url, listener);
//            if (!result && listener != null) {
//                listener.onCancel();
//            }
//        } catch (Throwable e) {
//            if (listener != null) {
//                listener.onException(e);
//            }
//        }
    }


    /**
     * 分享到朋友圈<br/>
     * 注：需要在WXEntryActivity的onCreate,onNewIntent里调用本类的onWeixinResultData方法，listener才会响应
     *
     * @param title       标题 可空。长度不超过512，超出将被截断
     * @param description 描述 可空。长度不超过1024，超出将被截断
     * @param thumbData   图片 可空。长度不超过32768(32k)，超出将直接return false；
     * @param url         链接 可空。长度不能超过10240，超出将直接return false；
     * @param listener    可空。回调监听器
     */
    public static void shareToWeixinTimeline(Activity activity, String title,
                                             String description, byte[] thumbData,
                                             String url, ShareListener listener) {
        shareToWeixinTimeline(activity, 0, title, description, thumbData, null, null, url, listener);
    }

    /**
     * 微信登录<br/>
     * 注：需要在WXEntryActivity的onCreate,onNewIntent里调用本类的onWeixinResultData方法，listener才会响应
     *
     * @param listener 可空。回调监听器
     */
    public static void loginByWeixin(Activity activity, LoginListener listener) {
//        recordActivity(activity);
//        try {
//            boolean result = new LoginWeixinManager(activity).login(listener);
//            if (!result && listener != null) {
//                listener.onCancel();
//            }
//        } catch (Throwable e) {
//            if (listener != null) {
//                listener.onException(e);
//            }
//        }
    }

    /**
     * 打开小程序
     *
     * @param activity        activity
     * @param userName        小程序原始id
     * @param path            拉起小程序页面的可带参路径，不填默认拉起小程序首页
     * @param miniprogramType WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE 可选打开 开发版，体验版和正式版
     * @param listener        回调监听
     */
//    public static void launchMiniProgram(Activity activity, String userName, String path,
//                                         int miniprogramType, MiniProgramListener listener) {
//        launchMiniProgram(activity, userName, path, miniprogramType, listener, null);
//    }

    /**
     * 打开小程序
     *
     * @param activity        activity
     * @param userName        小程序原始id
     * @param path            拉起小程序页面的可带参路径，不填默认拉起小程序首页
     * @param miniprogramType WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE 可选打开 开发版，体验版和正式版
     * @param listener        回调监听
     * @param callback        微信小程序回传数据
     */
//    public static void launchMiniProgram(Activity activity, String userName, String path,
//                                         int miniprogramType, MiniProgramListener listener,
//                                         MiniProgramCallback callback) {
//        recordActivity(activity);
//        try {
//            boolean result = new WeixinMiniProgramManager(activity)
//                    .setWxCallback(callback)
//                    .launchMiniProgram(userName, path, miniprogramType, listener);
//            if (listener != null && !result) {
//                listener.onCancel();
//            }
//        } catch (Throwable e) {
//            if (listener != null) {
//                listener.onException(e);
//            }
//        }
//    }

    /**
     * 调用分享后会打开微博自带的编辑框<br/>
     * 注：需要在Activity的onCreate,onNewIntent方法里调用本类的onActivityResultData方法，listener才会响应。
     *
     * @param text     可空。长度不能超过1024（注：官方文档里写着1024，不过有些版本是2000），由于用户可自己编辑，故不作校验。
     * @param bitmap   可空。大小不能超过2M
     * @param listener 可空。回调监听器
     */
    public static void shareToWeibo(Activity activity, String text, Bitmap bitmap,
                                    ShareListener listener) {
//        recordActivity(activity);
//        try {
//            boolean result = new ShareWeiboManager(activity).share(text, bitmap, listener);
//            if (!result && listener != null) {
//                listener.onCancel();
//            }
//        } catch (Throwable e) {
//            if (listener != null) {
//                listener.onException(e);
//            }
//        }
    }

    /**
     * WXEntryActivityde onCreate/onNewIntent回调，处理微信回调数据
     * <p>
     * 原方法体：
     * <p>
     * try {
     * if (checkActivity()) {
     * LoginWeixinManager.onResultData(data);
     * ShareWeixinManager.onResultData(data);
     * }
     * } catch (Throwable e) {
     * }
     * <p>
     * 由于微信通过新起一个Activity的方式实现回调。在跨进程情况下会有问题，
     * 启动分享时使用的是remote进程里的回调的是主进程的{@link ShareLoginManager}，
     * {@link ShareLoginManager}，而主进程里的该类并未调用{@link ShareLoginManager#recordActivity}
     * 方法,因此{@link ShareLoginManager#checkActivity}返回必定是false。针对对这种情况，
     * 返回是否回调成功。
     *
     * @param data
     */
    public static boolean onWeixinResultData(Intent data) {
//        try {
//            if (checkActivity()) {
//                LoginWeixinManager.onResultData(data);
//                ShareWeixinManager.onResultData(data);
//                WeixinMiniProgramManager.onResultData(data);
//                return true;
//            }
//        } catch (Throwable e) {
//        }

        return false;
    }


    /**
     * onCreate/onNewIntent回调，(主要处理微博回调数据)
     */
    public static void onActivityResultData(Activity activity, Intent data) {
//        try {
//            if (checkActivity()) {
//                ShareWeiboManager.onResultData(activity, data);
//            }
//        } catch (Throwable e) {
//        }
    }

    /**
     * onActivityResult回调，(主要处理QQ分享、登录相关回应)
     */
    public static void onActivityResult(int requestCode, int resultCode, Intent data) {
//        try {
//            if (checkActivity()) {
//                Tencent.onActivityResultData(requestCode, resultCode, data, null);
//            }
//        } catch (Throwable e) {
//        }
    }


    private static void recordActivity(Activity activity) {
        sActivityWeak = new WeakReference<Activity>(activity);
    }

    private static boolean checkActivity() {
        if (sActivityWeak == null) {
            return false;
        }
        return sActivityWeak.get() != null;
    }

}
