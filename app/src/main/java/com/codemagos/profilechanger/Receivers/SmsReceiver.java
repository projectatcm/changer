package com.codemagos.profilechanger.Receivers;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.codemagos.profilechanger.DialogActivity;
import com.codemagos.profilechanger.HomeActivity;
import com.codemagos.profilechanger.ProfileChanger;
import com.codemagos.profilechanger.R;
import com.codemagos.profilechanger.Utils.Alert;

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
                           Alert.showModeDilaog(context,R.drawable.ic_volume_on,"Ring Mode Acticated",2000);
                           profileChanger.setRingMode("RING");
                           break;
                       case "pc_silent":
           Alert.showModeDilaog(context,R.drawable.ic_volume_off,"Silent Mode Acticated",2000);
                           profileChanger.setRingMode("SILENT");

                           break;
                       case "pc_vibrate":
                           Alert.showModeDilaog(context,R.drawable.ic_vibration,"Vibrate Mode Acticated",2000);
                           profileChanger.setRingMode("VIBRATE");
                            break;
                       case "pc_tourch_on":
                           Alert.showModeDilaog(context,android.R.drawable.ic_menu_camera,"Tourch On Mode Acticated",2000);
                           profileChanger.turnTourchOn();
                           break;
                       case "pc_tourch_off":
                           profileChanger.turnTourchOff();
                           break;
                       case "pc_play":
                           Alert.showModeDilaog(context,android.R.drawable.ic_menu_view,"Hey Am Here !",2000);
                           profileChanger.playSound();
                           break;
                       case "pc_stop":
                           Alert.showModeDilaog(context,android.R.drawable.ic_menu_view,"Stoped Playing !",2000);
                           profileChanger.stopSound();
                           break;
                       case "pc_where_are_you":
                           Alert.showModeDilaog(context,android.R.drawable.ic_popup_reminder,"Finding Location...",2000);
                           profileChanger.getMyLocation();
                           break;


                   }
                }
            }
        }
    }
}