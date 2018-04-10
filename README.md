# 升级SDK（UpgradeSDK）
###### 简介
> 将系统常用的升级功能独立抽取出来作为一个SDK，此SDK提供强制更新和普通后台更新两种；
###### JCenter地址
     compile 'com.yqshi.sdk.upgrade:AndroidUpdate:1.0.1'

###### 权限要求
     <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
    <uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />

###### UpgradeSDK
      /**
       *单例模式
       */
      public static UpgradeSDK getInstance() {
        if (upgradesdk == null) {
            upgradesdk = new UpgradeSDK();
        }
        return upgradesdk;
    }

     /**
     * update app
     *
     * @param context      上下文
     * @param upgradeMessage 回调的内容
     * @param appName      appName
     */
    public void updateApp(Context context, UpgradeMessage upgradeMessage,
                          String appName) {
        UpgradeController.getInstance().updateApp(context, upgradeMessage,
                appName);
    }
###### 代码调用
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
                //message.setForce("UpgradeMessage.FORCE");
                message.setForce(UpgradeMessage.UN_FORCE);
                //设置下载的URL
                message.setDownloadUrl("http://weixin.qq.com/cgi-bin/download302?check=false&uin=&stype=&promote=&fr=&lang=zh_CN&ADTAG=&url=android16");
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

### github地址
[https://github.com/yqshi/Upgrade](https://github.com/yqshi/Upgrade)
### 博客地址
[http://blog.csdn.net/s939432817/article/details/52252483](http://blog.csdn.net/s939432817/article/details/52252483)
