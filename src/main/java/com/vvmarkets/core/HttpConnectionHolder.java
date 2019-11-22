package com.vvmarkets.core;

import com.vvmarkets.configs.Config;

public class HttpConnectionHolder {
    public static HttpConnectionHolder INSTANCE = new HttpConnectionHolder();

    private boolean isNetworkReachable = true;
    private long NetworkUnreachableStart;

    public boolean getIsNetworkReachable(){
        return isNetworkReachable;
    }

    public void connectionUnavailable() {
        if (isNetworkReachable) {
            NetworkUnreachableStart = System.currentTimeMillis();
            isNetworkReachable = false;

            DialogUtil.showWarningNotification("Нет соединения", "Невозможно соедениться с сервером, проверьте сетевое соединение");
        }
    }

    public void connectionAvailable() {
        if (!isNetworkReachable) {
            isNetworkReachable = true;
            DialogUtil.showInformationNotification("Соединение восстановленно", "Соеденение с сервером восстановленно");
        }
    }

    public boolean shouldRetry() {
        return isNetworkReachable || (System.currentTimeMillis() - NetworkUnreachableStart) / 1000 > Config.getNetworkRetryTimeout();
    }
}
