package com.codemagos.profilechanger;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.provider.Settings;

/**
 * Created by prasanth on 9/3/17.
 */

public class PhoneSettingsChanger {

    public static void brightness(Context context, int brightness) {
        Settings.System.putInt(context.getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS, brightness);
    }

    public static void wifi(Context context, boolean isTurnToOn) {
        WifiManager wifiManager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(isTurnToOn);
    }

    public static String getBrightness(Context context){
        String brightness = null;
        try {
            float curBrightnessValue= Settings.System.getInt(
                    context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
            brightness = String.valueOf(curBrightnessValue);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return brightness;
    }

}
