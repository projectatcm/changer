package com.codemagos.profilechanger.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.widget.Toast;

import com.codemagos.profilechanger.DbConnection.DbHelper;
import com.codemagos.profilechanger.PhoneSettingsChanger;
import com.codemagos.profilechanger.ProfileChanger;

/**
 * Created by prasanth on 9/3/17.
 */

public class WifiReceiver extends BroadcastReceiver {
    final String ACTION = "";
    DbHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;

    @Override
    public void onReceive(Context context, Intent intent) {
        dbHelper = new DbHelper(context);
        sqLiteDatabase = dbHelper.getWritableDatabase();
        NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
        if(info != null && info.isConnected()) {
            // Do your work.

            // e.g. To check the Network Name or other info:
            WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            String ssid = wifiInfo.getSSID();
            Toast.makeText(context,ssid,Toast.LENGTH_LONG).show();
            Cursor wifiActionCursor = dbHelper.getWifiAction(sqLiteDatabase,ssid);
            if(wifiActionCursor != null){
                wifiActionCursor.moveToNext();
                int profile = wifiActionCursor.getInt(2);
                ProfileChanger profileChanger = new ProfileChanger(context);
                profileChanger.set(profile+"");
                Toast.makeText(context,""+profile,Toast.LENGTH_LONG).show();
            }
        }
    }
}
