package com.codemagos.profilechanger.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.codemagos.profilechanger.PhoneSettingsChanger;

/**
 * Created by prasanth on 9/3/17.
 */

public class ActionReceiver extends BroadcastReceiver{
    final String ACTION_SMS = "android.provider.Telephony.SMS_RECEIVED";
    final String ACTION_AIRPLANE = Intent.ACTION_AIRPLANE_MODE_CHANGED;
    final String ACTION_PHONE_CALL = "";
    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()){
            case ACTION_AIRPLANE:
                // on air plane action
                Toast.makeText(context,PhoneSettingsChanger.getBrightness(context),Toast.LENGTH_LONG).show();
                break;
            case ACTION_SMS:
                Toast.makeText(context,"Message",Toast.LENGTH_LONG).show();
                PhoneSettingsChanger.brightness(context,20);

        }
    }
}
