package com.susu.baselibrary.utils.ui;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.DrawableRes;
import androidx.core.app.NotificationCompat;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Author : sudan
 * Time : 2021/7/27
 * Description:
 */
public class NotificationUtils {
    private static final long NOTIFICATION_TIME_OUT = 2 * 1000;

    private NotificationManager manager;

    private static NotificationUtils instance;

    private static Context mContext;

    private long mLstNotifiTime = 0;

    public static final String INNER_CHANNEL_CHAT = "CHAT_CHANNEL";
    public static final String INNER_CHANNEL_CHAT_NAME = "聊天推送";
    public static final String INNER_CHANNEL_DOWNLOAD = "DOWNLOAD_CHANNEL";
    public static final String INNER_CHANNEL_DOWNLOAD_NAME = "下载进度";

    private NotificationCompat.Builder mChatBuilder;
    private NotificationCompat.Builder mDownloadBuilder;

    private NotificationUtils(Context context) {
        manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel chatChannel = new NotificationChannel(INNER_CHANNEL_CHAT, INNER_CHANNEL_CHAT_NAME, NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(chatChannel);

            NotificationChannel downChannel = new NotificationChannel(INNER_CHANNEL_DOWNLOAD, INNER_CHANNEL_DOWNLOAD_NAME, NotificationManager.IMPORTANCE_LOW);
            manager.createNotificationChannel(downChannel);
        }
    }


    public static NotificationUtils getInstance(Context context) {
        mContext = context.getApplicationContext();
        if (instance == null) {
            instance = new NotificationUtils(context.getApplicationContext());
        }
        return instance;
    }


    /**
     * @param notificationId
     */
    public void cancel(int notificationId) {
        manager.cancel(notificationId);
    }


    /**
     * init download notification builder,set default config.
     *
     * @param icon
     */
    public void initDownloadBuilder(int icon, String title, String ticker) {
        Intent intent = new Intent();
        PendingIntent contentIntent = PendingIntent.getActivity(mContext,
                0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mDownloadBuilder = new NotificationCompat.Builder(mContext, INNER_CHANNEL_DOWNLOAD).
                setSmallIcon(icon)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setTicker(ticker)
                .setContentIntent(contentIntent);
    }

    /**
     * send a download progress notification with {@link #mDownloadBuilder}.
     * if you haven`t called {@link #initDownloadBuilder(int, String, String)},
     * we will create a builder with default config.
     *
     * @param notificationId
     * @param progress
     * @param length
     * @param count
     */
    @SuppressWarnings("unused")
    public void sendDownloadNotify(int notificationId, String progress, int length, int count) {
        sendDownloadNotify(notificationId,
                "正在下载",
                "正在下载更新...",
                0,
                progress,
                length,
                count,
                -1);

    }

    /**
     * send a download text notification with {@link #mDownloadBuilder}.
     *
     * @param notificationId
     * @param text
     */
    @SuppressWarnings("unused")
    public void sendDownloadNotify(int notificationId, String text) {
        sendDownloadNotify(
                notificationId,
                "正在下载",
                "正在下载更新...",
                0,
                text,
                0, 0, -1
        );
    }

    public void sendDownloadNotify(int notificationId, String title, String ticker, @DrawableRes int icon, String text,
                                   int length, int count, int flag) {
        if (mDownloadBuilder == null) {
            initDownloadBuilder(icon, title, ticker);
        }

        Notification notification = mDownloadBuilder
                .setContentText(text)
                .setProgress(length, count, false)
                .build();

        if (-1 != flag) {
            notification.flags = flag;
        }

        manager.notify(notificationId, notification);
    }

    public void sendCustomNotify(int notificationId, NotificationCompat.Builder builder) {
        manager.notify(notificationId, builder.build());
    }

    /**
     * send a chat notification.
     *
     * @param notificationId
     * @param title
     * @param text
     * @param icon
     * @param audioEnable
     * @param vibrateEnable
     * @param intent
     */
    public void showChatNotification(int notificationId, String title, String text, @DrawableRes int icon, boolean audioEnable, boolean vibrateEnable, Intent intent) {
        showNotify(notificationId, INNER_CHANNEL_CHAT, title, text, icon, audioEnable, vibrateEnable, intent);
    }


    /**
     * create notification channel。
     */
    public void createNotificationChannel(String channelId, String channelName, int importance) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            manager.createNotificationChannel(channel);
        }
    }

    /**
     * send a notification.
     * before this,you should call {@link #createNotificationChannel(String, String, int)}
     * to create a channel.the notification with same channel well be divided into one group。
     *
     * @param notificationId
     */
    public void showNotify(int notificationId, String channelId, String title, String text, @DrawableRes int icon, boolean audioEnable, boolean vibrateEnable, Intent intent) {

        int defaults = Notification.DEFAULT_LIGHTS;
        long currTime = System.currentTimeMillis();
        if (currTime - this.mLstNotifiTime >= NOTIFICATION_TIME_OUT) {
            if (audioEnable) {
                defaults |= Notification.DEFAULT_SOUND;
            }
            if (vibrateEnable) {
                defaults |= Notification.DEFAULT_VIBRATE;
            }
            this.mLstNotifiTime = currTime;
        }

        manager.notify(notificationId,
                createNotification(defaults, channelId, title, text, icon, intent));
    }

    /**
     * 创建notification
     */
    private Notification createNotification(int defaults, String channelId, String title, String text, @DrawableRes int icon, Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, channelId).
                setSmallIcon(icon).
                setTicker(text).
                setAutoCancel(true).
                setContentTitle(title).
                setContentText(text).
                setDefaults(defaults);
        if (null != intent) {
            builder.setContentIntent(PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));
        }
        return builder.build();
    }
}
