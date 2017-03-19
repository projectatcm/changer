package com.codemagos.profilechanger;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.codemagos.profilechanger.DbConnection.DbHelper;

/**
 * Created by prasanth on 16/3/17.
 */

public class ProfileChanger {
    AudioManager audioManager;
    Context context;
    DbHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;
    BluetoothAdapter mBluetoothAdapter;
    WifiManager wifiManager;

    public ProfileChanger(Context context) {
        this.context = context;
        dbHelper = new DbHelper(context);
        sqLiteDatabase = dbHelper.getWritableDatabase();
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    }

    public void set(String profileID) {
        Cursor cursor = dbHelper.getProfile(sqLiteDatabase, profileID);
        if (cursor != null) {
            cursor.moveToNext();

            String profile_name = cursor.getString(1);
            String airplane = cursor.getString(2);
            String bluetooth = cursor.getString(3);
            String brightness = cursor.getString(4);
            String gps = cursor.getString(5);
            String mobile_data = cursor.getString(6);
            String ring = cursor.getString(7);
            String wifi = cursor.getString(8);
            Log.d("PROFILE NAME", profile_name);
            Log.d("airplane", airplane);
            Log.d("PROFILE bluetooth", bluetooth);
            Log.d("PROFILE brightness", brightness);
            Log.d("PROFILE gps", gps);
            Log.d("PROFILE mobile_data", mobile_data);
            Log.d("PROFILE ring", ring);
            Log.d("PROFILE wifi", wifi);
            setScreenBrightness(Integer.parseInt("10"));
            setRingMode(ring);
        }
    }

    public void setRingMode(String mode) {
        switch (mode) {
            case "VIBRATE":
                audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                break;
            case "SILENT":
                audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                break;
            case "RING":
                audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                break;
        }
    }

    public void setWifi(String state) {
        boolean wifiEnabled = wifiManager.isWifiEnabled();
        if (state == "on" && !wifiEnabled) {
            wifiManager.setWifiEnabled(true);
        } else {
            wifiManager.setWifiEnabled(false);
        }
    }

    public void setBluetooth(String state) {
        if (state == "on") {
            if (!mBluetoothAdapter.isEnabled()) {
                mBluetoothAdapter.enable();
            }
        } else {
            if (mBluetoothAdapter.isEnabled()) {
                mBluetoothAdapter.disable();
            }
        }
    }


    public void setScreenBrightness(int brightnessValue) {
        /*
            public abstract ContentResolver getContentResolver ()
                Return a ContentResolver instance for your application's package.
        */
        /*
            Settings
                The Settings provider contains global system-level device preferences.

            Settings.System
                System settings, containing miscellaneous system preferences. This table holds
                simple name/value pairs. There are convenience functions for accessing
                individual settings entries.
        */
        /*
            public static final String SCREEN_BRIGHTNESS
                The screen backlight brightness between 0 and 255.
                Constant Value: "screen_brightness"
        */
        /*
            public static boolean putInt (ContentResolver cr, String name, int value)
                Convenience function for updating a single settings value as an integer. This will
                either create a new entry in the table if the given name does not exist, or modify
                the value of the existing row with that name. Note that internally setting values
                are always stored as strings, so this function converts the given value to a
                string before storing it.

            Parameters
                cr : The ContentResolver to access.
                name : The name of the setting to modify.
                value : The new value for the setting.
            Returns
                true : if the value was set, false on database errors
        */

        // Make sure brightness value between 0 to 255
        if (brightnessValue >= 0 && brightnessValue <= 255) {
            Settings.System.putInt(
                    context.getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS,
                    brightnessValue
            );
        }
    }

    // Get the screen current brightness
    protected int getScreenBrightness() {
        /*
            public static int getInt (ContentResolver cr, String name, int def)
                Convenience function for retrieving a single system settings value as an integer.
                Note that internally setting values are always stored as strings; this function
                converts the string to an integer for you. The default value will be returned
                if the setting is not defined or not an integer.

            Parameters
                cr : The ContentResolver to access.
                name : The name of the setting to retrieve.
                def : Value to return if the setting is not defined.
            Returns
                The setting's current value, or 'def' if it is not defined or not a valid integer.
        */
        int brightnessValue = Settings.System.getInt(
                context.getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS,
                0
        );
        return brightnessValue;
    }


    public boolean getAirplaneMode() {
        try {
            int airplaneModeSetting = Settings.System.getInt(context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON);
            return airplaneModeSetting == 1 ? true : false;
        } catch (Settings.SettingNotFoundException e) {
            return false;
        }
    }

    public boolean getWifiState() {
        boolean wifiEnabled = wifiManager.isWifiEnabled();
        return wifiEnabled ? true : false;
    }

    public boolean getBlueToothState() {
        boolean bluetoothState = mBluetoothAdapter.isEnabled();
        return bluetoothState ? true : false;
    }


}
