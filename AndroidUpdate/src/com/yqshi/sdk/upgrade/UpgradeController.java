package com.yqshi.sdk.upgrade;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

class UpgradeController {
    private static UpgradeController upgradeController;
    /**
     * appUpdateNotificationHelper
     */
    private UpgradeNotificationHelper upgradeNotificationHelper;
    /**
     * the max length of file
     */
    private int max;
    /**
     * the procress of download
     */
    private int currentProcress;
    /**
     * whether isloading
     */
    private boolean isLoading = true;
    /**
     * updateHandler
     */
    private Handler updateHandler;

    /**
     * get single instance 0f UpgradeController
     */
    public static UpgradeController getInstance() {
        if (upgradeController == null) {
            upgradeController = new UpgradeController();
        }
        return upgradeController;
    }

    /**
     * update app
     */
    protected void updateApp(Context context, UpgradeMessage upgradeMessage,
                             String appName) {
        // ����force���ж��Ƿ���ǿ������
        if (UpgradeMessage.FORCE.equals(upgradeMessage.getForce())) {
            downLoadApk(context, upgradeMessage.getDownloadUrl());
        } else {
            downLoadApkBackground(context, upgradeMessage.getDownloadUrl(),
                    appName);
        }
    }

    /**
     * download apk it is force
     * <p/>
     * (final Context context, final String downLoadAPPurl)rl
     */
    protected void downLoadApk(final Context context,
                               final String downLoadAPPurl) {
        final ProgressDialog pd; // �������Ի���
        pd = new ProgressDialog(context);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setMessage(context.getString(R.string.ck_updateing));
        pd.setCancelable(false);
        // �����ذ�װʱ�����������ط�������ʧ������
        pd.setCanceledOnTouchOutside(false);
        //pd.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        pd.show();
        new Thread() {
            @Override
            public void run() {
                try {
                    File file = getFileFromServer(downLoadAPPurl, pd);
                    sleep(3000);
                    installApk(context, file);
                    pd.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                    // Message msg = new Message();
                    // msg.what = DOWN_ERROR;
                    // handler.sendMessage(msg);
                }
            }
        }.start();
    }

    /**
     * down load apk in background
     */
    @SuppressLint("HandlerLeak")
    protected void downLoadApkBackground(final Context context,
                                         final String downLoadAPPurl, String appName) {
        upgradeNotificationHelper = new UpgradeNotificationHelper(context, appName);
        upgradeNotificationHelper.showNotification(true);

        try {
            // ���d��Thread
            new Thread() {
                @Override
                public void run() {
                    try {
                        File file = getFileFromServer(downLoadAPPurl);
                        sleep(3000);
                        upgradeNotificationHelper.showNotification(false);
                        isLoading = true;
                        currentProcress = 0;
                        installApk(context, file);
                    } catch (Exception e) {
                        e.printStackTrace();
                        try {
                            throw new FileNotFoundException("�����Ƿ���ж�д�ļ���Ȩ��");
                        } catch (FileNotFoundException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }.start();

        } catch (Exception e) {
            e.printStackTrace();
        }

        // ���ص�Handel
        updateHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (currentProcress >= 100) {
                    isLoading = false;
                }
                if (upgradeNotificationHelper != null) {
                    upgradeNotificationHelper.update(context, currentProcress);
                }
            }

        };

        new Thread() {
            @Override
            public void run() {
                try {
                    while (isLoading) {
                        Thread.sleep(1000);
                        updateHandler.sendEmptyMessage(0);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /***
     * get file from server
     *
     * @param path
     * @param pd
     * @return
     * @throws Exception
     */
    @SuppressLint("NewApi")
    private File getFileFromServer(String path, ProgressDialog pd)
            throws Exception {
        // �����ȵĻ���ʾ��ǰ��sdcard�������ֻ��ϲ����ǿ��õ�
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            if (android.os.Build.VERSION.SDK_INT >= 11) {
                pd.setProgressNumberFormat("%1dM/%2dM");
            }
            // ��ȡ���ļ��Ĵ�С
            pd.setMax(conn.getContentLength() / (1024 * 1024));
            InputStream is = conn.getInputStream();
            File file = new File(Environment.getExternalStorageDirectory(),
                    "updata.apk");
            if (file.exists()) {
                file.delete();
            }
            FileOutputStream fos = new FileOutputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];
            int len;
            int total = 0;
            while ((len = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
                total += len;

                pd.setProgress(total / (1024 * 1024));
            }
            fos.close();
            bis.close();
            is.close();
            return file;
        } else {
            return null;
        }
    }

    /**
     * get file from server
     *
     * @param path
     * @return
     * @throws Exception
     */
    private File getFileFromServer(String path) throws Exception {
        // �����ȵĻ���ʾ��ǰ��sdcard�������ֻ��ϲ����ǿ��õ�
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);

            // ��ȡ���ļ��Ĵ�С
            max = conn.getContentLength();
            InputStream is = conn.getInputStream();
            File file = new File(Environment.getExternalStorageDirectory(),
                    "updata.apk");
            if (file.exists()) {
                file.delete();
            }
            FileOutputStream fos = new FileOutputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];
            int len;
            int total = 0;
            while ((len = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
                total += len;
                currentProcress = (total * 100) / max;
            }

            fos.close();
            bis.close();
            is.close();
            return file;
        } else {
            return null;
        }
    }

    /**
     * install apk
     *
     * @param file
     */
    private void installApk(Context context, File file) {
        Intent intent = new Intent();
        // ִ�ж���
        intent.setAction(Intent.ACTION_VIEW);
        // ����TAG
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
         //7.0�����߲�ͬ�ķ���
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri apkUri = FileProvider.getUriForFile(context,
                    context.getApplicationContext().getPackageName()+".fileprovider", file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
            //����8.0
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                boolean hasInstallPermission = context.getPackageManager().canRequestPackageInstalls();
                if (!hasInstallPermission) {
                    startInstallPermissionSettingActivity(context);
                    return;
                }
            }

        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
    }

    /**
     * ��ת������-����װδ֪��Դ-ҳ��
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startInstallPermissionSettingActivity(Context context) {
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
