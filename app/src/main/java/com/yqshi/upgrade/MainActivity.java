package com.yqshi.upgrade;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.yqshi.sdk.upgrade.UpgradeMessage;
import com.yqshi.sdk.upgrade.UpgradeSDK;
import com.yqshi.sdk.upgrade.UpgradeViewInterface;


public class MainActivity extends AppCompatActivity implements UpgradeViewInterface<UpgradeMessage> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkVersion(1);
        //checkVersion(0);
        //checkVersion(-1);
    }

//    /**
//     * 获取屏幕（分辨率）
//     */
//    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
//    private void getDisplayInfomation() {
//        Point point = new Point();
//        getWindowManager().getDefaultDisplay().getSize(point);
//        Log.d(TAG, "the screen size is " + point.toString());
//        getWindowManager().getDefaultDisplay().getRealSize(point);
//        Log.d(TAG, "the screen real size is " + point.toString());
//    }
//
//    /**
//     * 获取屏幕密度
//     */
//    private void getDensity() {
//        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
//        Log.d(TAG, "Density is " + displayMetrics.density + " densityDpi is " + displayMetrics.densityDpi + " height: " + displayMetrics.heightPixels +
//                " width: " + displayMetrics.widthPixels);
//    }
//
//    /**
//     * 获取屏幕尺寸
//     */
//    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
//    private void getScreenSizeOfDevice2() {
//        Point point = new Point();
//        getWindowManager().getDefaultDisplay().getRealSize(point);
//        DisplayMetrics dm = getResources().getDisplayMetrics();
//        double x = Math.pow(point.x / dm.xdpi, 2);
//        double y = Math.pow(point.y / dm.ydpi, 2);
//        double screenInches = Math.sqrt(x + y);
//        Log.d(TAG, "Screen inches : " + screenInches);
//    }

    /**
     * 模拟自己后台请求
     *
     * @param upgradeStatus 假设  1是新的 0是没有更新，-1是失败 具体的自己的项目自己代人
     * @return 模拟请求检测更新接口，根据检测更新接口不同状态去调用不同方法
     */
    private void checkVersion(int upgradeStatus) {
        switch (upgradeStatus) {
            case 1:
                UpgradeMessage message = new UpgradeMessage();
                //设置提示描述信息
                message.setDesc("新版本更新");
                //设置是否强制升级 1：强制 0：非强制
                // message.setForce("1");
                message.setForce("1");
                //设置下载的URL
                message.setDownloadUrl("https://dev-xsimages.51lick.cn/ware/files/apk/file/20200721/1595323690657820.apk");
                ckNew(message);
                break;
            case 0:
                ckOriginal();
                break;
            case -1:
                ckFailed();
                break;
            default:
                break;
        }
    }

    @Override
    public void ckNew(UpgradeMessage upMsg) {
        //调用升级方法
        UpgradeSDK.getInstance().updateApp(this, upMsg, getString(R.string.app_name));
    }

    @Override
    public void ckOriginal() {
        Toast.makeText(this, "当前已经是最新版本", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void ckFailed() {
        Toast.makeText(this, "检查更新失败", Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("22", "pppppppp" + resultCode + "-------" + requestCode);
        if (resultCode == RESULT_OK && requestCode == UpgradeSDK.REQUEST_CODE_INSTALL_SETTING_UNKNOW_APP) {
            UpgradeSDK.getInstance().installApp(MainActivity.this);
        }
    }

    /**
     * 自己写的调用通知栏
     */
    private void startNotification() {
        Log.i("NextActivity", "startNotification");

        // Sets an ID for the notification
        int mNotificationId = 001;

        // Build Notification , setOngoing keeps the notification always in status bar
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                        .setContentTitle("Stop LDB")
                        .setContentText("Click to stop LDB")
                        .setOngoing(true);

        // Create pending intent, mention the Activity which needs to be
        //triggered when user clicks on notification(StopScript.class in this case)

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(contentIntent);
        mBuilder.setAutoCancel(true);

        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());


    }


}
