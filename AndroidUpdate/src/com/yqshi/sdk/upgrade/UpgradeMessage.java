package com.yqshi.sdk.upgrade;

public class UpgradeMessage {

    /**
     * error network code
     */
    public static final String FORCE = "1";
    /**
     * error server
     */
    public static final String UN_FORCE = "0";


    /**
     * appStatusMessage
     */
    private String appStatusMessage;
    /**
     * new app url
     */
    private String downloadUrl;
    /**
     * whether app update is force
     */
    private String force;
    /**
     * new app description
     */
    private String desc;
    /**
     * 1 new app 0 no new app
     */
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getForce() {
        return force;
    }

    public void setForce(String force) {
        this.force = force;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getAppStatusMessage() {
        return appStatusMessage;
    }

    public void setAppStatusMessage(String appStatusMessage) {
        this.appStatusMessage = appStatusMessage;
    }

}
