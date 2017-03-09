package com.codemagos.profilechanger.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.widget.Toast;

import com.codemagos.profilechanger.PhoneSettingsChanger;

/**
 * Created by prasanth on 9/3/17.
 */

public class WifiReceiver extends BroadcastReceiver {
    final String ACTION = "";

    @Override
    public void onReceive(Context context, Intent intent) {
        NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
        if(info != null && info.isConnected()) {
            // Do your work.

            // e.g. To check the Network Name or other info:
            WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            String ssid = wifiInfo.getSSID();
            Toast.makeText(context,ssid,Toast.LENGTH_LONG).show();
        }
    }
}
