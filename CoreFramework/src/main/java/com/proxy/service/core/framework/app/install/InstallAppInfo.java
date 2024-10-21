package com.proxy.service.core.framework.app.install;

import android.graphics.drawable.Drawable;
import android.os.Build;

import androidx.annotation.RequiresApi;

/**
 * 已安装app的信息
 *
 * @author: cangHX
 * on 2020/06/24  17:14
 */
public class InstallAppInfo {

    /**
     * app名字
     */
    public String name;
    /**
     * app图标
     */
    public Drawable icon;
    /**
     * app包名
     */
    public String packageName;
    /**
     * app版本name
     */
    public String versionName;
    /**
     * app版本code
     *
     * @see #longVersionCode
     */
    @Deprecated
    public int versionCode;
    /**
     * app版本code
     */
    @RequiresApi(Build.VERSION_CODES.P)
    public long longVersionCode;
    /**
     * 是否安装在sd卡
     */
    public boolean isInstallSd;
    /**
     * 是否是系统应用
     */
    public boolean isSystemApp;

    @Override
    public String toString() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            return "InstallAppInfo{" +
                    "name='" + name + '\'' +
                    ", icon=" + icon +
                    ", packageName='" + packageName + '\'' +
                    ", versionName='" + versionName + '\'' +
                    ", versionCode=" + versionCode +
                    ", longVersionCode=" + longVersionCode +
                    ", isInstallSd=" + isInstallSd +
                    ", isSystemApp=" + isSystemApp +
                    '}';
        }else {
            return "InstallAppInfo{" +
                    "name='" + name + '\'' +
                    ", icon=" + icon +
                    ", packageName='" + packageName + '\'' +
                    ", versionName='" + versionName + '\'' +
                    ", versionCode=" + versionCode +
                    ", isInstallSd=" + isInstallSd +
                    ", isSystemApp=" + isSystemApp +
                    '}';
        }
    }
}
