package com.codemagos.profilechanger.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.codemagos.profilechanger.DbConnection.DbHelper;
import com.codemagos.profilechanger.ProfileChanger;
import com.codemagos.profilechanger.Spstore.SharedPreferenceStore;

/**
 * Created by prasanth on 18/3/17.
 */

public class CallReceiver extends BroadcastReceiver {
    SharedPreferenceStore spStore;
    DbHelper dbHelper;
    SQLiteDatabase db;
    ProfileChanger profileChanger;
    AudioManager audioManager;
    @Override
    public void onReceive(Context context, Intent intent) {
         /*---------------------*/
        spStore = new SharedPreferenceStore(context);
        dbHelper = new DbHelper(context);
        db = dbHelper.getWritableDatabase();
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        /*--------------------------*/

        ProfileChanger profileChanger = new ProfileChanger(context);
        try {
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            Cursor cursor = dbHelper.searchInContact(db,incomingNumber);
            Toast.makeText(context,spStore.getPrevRingMode()+"",Toast.LENGTH_LONG).show();

            if(state.equals(TelephonyManager.EXTRA_STATE_RINGING)){
                Log.d("Prev Mode",spStore.getPrevRingMode()+"");
                if(cursor != null){
                    cursor.moveToNext();
                    Toast.makeText(context,cursor.getString(1),Toast.LENGTH_LONG).show();
                    spStore.setPrevRingMode(audioManager.getRingerMode());
                    profileChanger.setRingMode(cursor.getString(3));
                }
                Toast.makeText(context,"Ringing State Number is - " + incomingNumber, Toast.LENGTH_SHORT).show();
            }
            if ((state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK))){
                Toast.makeText(context,"Received State",Toast.LENGTH_SHORT).show();
                profileChanger.setRingMode(spStore.getPrevRingMode());

            }
            if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)){
                Toast.makeText(context,"Idle State",Toast.LENGTH_SHORT).show();
               int prev_ring_mode = spStore.getPrevRingMode();

                if(prev_ring_mode != 0){
                    profileChanger.setRingMode(prev_ring_mode);
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
}
