package com.codemagos.profilechanger;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Camera;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
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
    MediaPlayer mediaPlayer;
    static Camera camera = null;
    Camera.Parameters camera_parameters;

    public ProfileChanger(Context context) {
        this.context = context;
        dbHelper = new DbHelper(context);
        sqLiteDatabase = dbHelper.getWritableDatabase();
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        mediaPlayer = MediaPlayer.create(context, R.raw.sound);
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
            setScreenBrightness(Integer.parseInt(brightness));
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

    public void setRingMode(int mode) {
        audioManager.setRingerMode(mode);
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
        if (wifiManager != null) {
            boolean wifiEnabled = wifiManager.isWifiEnabled();
            return wifiEnabled ? true : false;
        }
        return false;
    }

    public boolean getBlueToothState() {
        if (mBluetoothAdapter != null) {
            boolean bluetoothState = mBluetoothAdapter.isEnabled();
            return bluetoothState ? true : false;
        }
        return false;
    }


    public void turnTourchOn() {
        PackageManager pm = context.getPackageManager();
        if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            camera = Camera.open();
            camera_parameters = camera.getParameters();
            camera_parameters.setFlashMode(camera_parameters.FLASH_MODE_TORCH);
            camera.setParameters(camera_parameters);
            camera.startPreview();
        }
    }

    public void turnTourchOff() {
        camera.stopPreview();
        camera.release();
        camera = null;
    }

    public void playSound() {
        mediaPlayer.start();
    }

    public void stopSound() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }


    public void turnGPSOn() {
        String provider = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if (!provider.contains("gps")) { //if gps is disabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            context.sendBroadcast(poke);
        }
    }

    public void turnGPSOff() {
        String provider = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if (provider.contains("gps")) { //if gps is enabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            context.sendBroadcast(poke);

        }
    }

    public void getMyLocation() {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        sendLocationSMS("9072388801",location);


    }
    public void sendLocationSMS(String phoneNumber, Location currentLocation) {
        SmsManager smsManager = SmsManager.getDefault();
        StringBuffer smsBody = new StringBuffer();
        smsBody.append("http://maps.google.com?q=");
        smsBody.append(currentLocation.getLatitude());
        smsBody.append(",");
        smsBody.append(currentLocation.getLongitude());
        smsManager.sendTextMessage(phoneNumber, null, smsBody.toString(), null, null);
    }
}
