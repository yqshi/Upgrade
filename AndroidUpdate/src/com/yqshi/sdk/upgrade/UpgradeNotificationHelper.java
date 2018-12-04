package com.yqshi.sdk.upgrade;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.widget.RemoteViews;

class UpgradeNotificationHelper {
    /**
     * notificationManager
     */
    private NotificationManager nMgr;
    /**
     * notification
     */
    private Notification notification;
    /**
     * notify_id
     */
    private final int NOTIFY_ID = 201188;
    /**
     * progress max length
     */
    private int max = 100;
    /**
     * update
     */
    private String UPDATE = "";

    /**
     * constructorsUPDATE
     *
     * @param context
     * @param appName
     */

    public UpgradeNotificationHelper(Context context, String appName) {

        UPDATE = appName + context.getString(R.string.ck_update);

        nMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            //创建 通知通道  channelid和channelname是必须的（自己命名就好）
            NotificationChannel channel = new NotificationChannel("yqshi_upgrade",
                    "yqshi_upgrade_channel", NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(true);//是否在桌面icon右上角展示小红点
            channel.setLightColor(Color.RED);//小红点颜色
            channel.setShowBadge(true); //是否在久按桌面图标时显示此渠道的通知
            nMgr.createNotificationChannel(channel);

            int notificationId = 0x1234;
            Notification.Builder builder = new Notification.Builder(context,"yqshi_upgrade");

            //设置通知显示图标、文字等
            builder.setSmallIcon(ResourceUtil.getMipmapId(context, "ic_launcher"))
                    .setContentText(context.getString(R.string.ck_updated))
                    .setAutoCancel(true);
            notification=builder.build();
        }else {
            notification = new Notification(ResourceUtil.getMipmapId(context, "ic_launcher"),
                    context.getString(R.string.ck_updated), System.currentTimeMillis());
        }

        RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.ck_update_progressdialog);
        contentView.setImageViewResource(R.id.icon,
                ResourceUtil.getMipmapId(context, "ic_launcher"));
        contentView.setTextViewText(R.id.tvmsg, UPDATE);
        contentView.setProgressBar(R.id.pb, max, 0, false);

        notification.icon = ResourceUtil.getMipmapId(context, "ic_launcher");
        notification.contentView = contentView;

        Intent intent = new Intent();
        PendingIntent pendingIntent_BtnCancel = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        contentView.setOnClickPendingIntent(R.id.btn_cancel, pendingIntent_BtnCancel);

        notification.contentIntent = pendingIntent_BtnCancel;
    }

    /**
     * update notification progressBar
     *
     * @param context context
     * @param current current
     */
    public void update(Context context, int current) {

        if (notification.contentView != null) {
            notification.contentView.setTextViewText(R.id.tvmsg, UPDATE + current + "%");
            notification.contentView.setProgressBar(R.id.pb, max, current, false);
        }

        if (notification != null)
            nMgr.notify(NOTIFY_ID, notification);

    }

    /**
     * reSet notification progressBar
     *
     * @param context
     */
    public void reset(Context context) {
        if (notification.contentView != null) {
            notification.contentView.setTextViewText(R.id.tvmsg, UPDATE);
            notification.contentView.setProgressBar(R.id.pb, max, 1, false);
        }
    }

    /**
     * show notification
     *
     * @param isShow
     */
    public void showNotification(boolean isShow) {
        if (isShow) {
            if (notification != null)
                nMgr.notify(NOTIFY_ID, notification);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                //关闭通知通道
                nMgr.deleteNotificationChannel("yqshi_upgrade");
            }else {
                nMgr.cancel(NOTIFY_ID);
            }

        }
    }





}