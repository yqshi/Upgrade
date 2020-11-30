package com.yqshi.sdk.upgrade;

import android.content.Context;

public class UpgradeSDK {
    /**
     * UpgradeSDK
     */
    private static UpgradeSDK upgradeSDK;

    public static int REQUEST_CODE_INSTALL_SETTING_UNKNOW_APP = 162;

    private UpgradeSDK() {

    }

    public static UpgradeSDK getInstance() {
        if (upgradeSDK == null) {
            upgradeSDK = new UpgradeSDK();
        }
        return upgradeSDK;
    }


    /**
     * update app
     *
     * @param context        context
     * @param upgradeMessage UpgradeMessage
     * @param appName        appName
     */
    public void updateApp(Context context, UpgradeMessage upgradeMessage,
                          String appName) {
        UpgradeController.getInstance().updateApp(context, upgradeMessage,
                appName);
    }


    /**
     * update app
     *
     * @param context        context
     */
    public void installApp(Context context) {
        UpgradeController.getInstance().installApk(context);
    }

}
