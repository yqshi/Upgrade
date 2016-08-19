package com.yqshi.sdk.upgrade;

import android.content.Context;

public class UpgradeSDK {
    /**
     * UpgradeSDK
     */
    private static UpgradeSDK upgradeSDK;

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
     * @param context      context
     * @param upgradeMessage UpgradeMessage
     * @param appName      appName
     */
    public void updateApp(Context context, UpgradeMessage upgradeMessage,
                          String appName) {
        UpgradeController.getInstance().updateApp(context, upgradeMessage,
                appName);
    }

}
