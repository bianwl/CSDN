package com.vann.csdn.util;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;

/**
 * author： bwl on 2016-03-26.
 * email: bxl049@163.com
 */
public class NetUtil {

    /**
     * 判断当前是否有可用网络
     *
     * @param context
     * @return
     */
    public static boolean isOnline(Context context) {
        boolean wifiConnnected = isWifiConnnected(context);
        boolean mobileConnected = isMobileConnected(context);
        if (wifiConnnected == false && mobileConnected == false) {
            return false;
        }
        return true;
    }

    public static boolean isWifiConnnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getApplicationContext().
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (null != info && info.isConnected()) {
            return true;
        }
        return false;
    }

    public static boolean isMobileConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getApplicationContext().
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (null != info && info.isConnected()) {
            return true;
        }
        return false;
    }
}
