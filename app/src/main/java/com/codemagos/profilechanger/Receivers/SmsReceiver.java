package com.codemagos.profilechanger.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.codemagos.profilechanger.ProfileChanger;

/**
 * Created by prasanth on 19/3/17.
 */

public class SmsReceiver extends BroadcastReceiver {
ProfileChanger profileChanger;
    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    private static final String TAG = "SMSBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        profileChanger = new ProfileChanger(context);
        Log.i(TAG, "Intent recieved: " + intent.getAction());

        if (intent.getAction().equals(SMS_RECEIVED)) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[])bundle.get("pdus");
                final SmsMessage[] messages = new SmsMessage[pdus.length];
                for (int i = 0; i < pdus.length; i++) {
                    messages[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                }
                if (messages.length > -1) {
                    String message = messages[0].getMessageBody().toLowerCase();
                   /* Toast.makeText(context, "Message recieved: " + messages[0].getMessageBody(), Toast.LENGTH_LONG).show();*/
                   switch (message){
                       case "pc_ring":
                           profileChanger.setRingMode("RING");
                           break;
                       case "pc_silent":
                           profileChanger.setRingMode("SILENT");
                           break;
                       case "pc_vibrate":
                           profileChanger.setRingMode("VIBRATE");
                            break;
                       case "pc_tourch_on":
                           profileChanger.turnTourchOn();
                           break;
                       case "pc_tourch_off":
                           profileChanger.turnTourchOff();
                           break;
                       case "pc_play":
                           profileChanger.playSound();
                           break;
                       case "pc_stop":
                           profileChanger.stopSound();
                           break;
                       case "pc_where_are_you":
                           profileChanger.getMyLocation();
                           break;


                   }
                }
            }
        }
    }
}