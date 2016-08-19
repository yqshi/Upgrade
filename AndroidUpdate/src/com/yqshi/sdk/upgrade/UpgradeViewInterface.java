package com.yqshi.sdk.upgrade;


public interface UpgradeViewInterface<T> {
    /**
     * new app
     * @param upMsg upmsg
     */
    void ckNew(T upMsg);

    /**
     * origin app
     */
    void ckOriginal();

    /**
     * update fail
     */
    void ckFailed();

}
