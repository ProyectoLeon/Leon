package com.limeri.leon.common;

import android.content.Context;
import android.provider.Settings;

public class Application {
    private static Context applicationContext = null;
    private static String deviceId = null;

    public static Context getApplicationContext() {
        return applicationContext;
    }

    public static void setApplicationContext(Context context) {
        applicationContext = context;
        deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static String getDeviceId() {
        return deviceId;
    }
}
