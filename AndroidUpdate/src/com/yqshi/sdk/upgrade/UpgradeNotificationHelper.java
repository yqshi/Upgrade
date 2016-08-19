package com.yqshi.sdk.upgrade;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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
        notification = new Notification(ResourceUtil.getMipmapId(context, "ic_launcher"),
                context.getString(R.string.ck_updated), System.currentTimeMillis());

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
            nMgr.cancel(NOTIFY_ID);
        }
    }

}