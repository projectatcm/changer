package com.codemagos.profilechanger;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.codemagos.profilechanger.DbConnection.DbHelper;
import com.codemagos.profilechanger.Spstore.SharedPreferenceStore;
import com.codemagos.profilechanger.Utils.Utils;

public class HomeActivity extends AppCompatActivity {
    SharedPreferenceStore spStore;
    DbHelper dbHelper;
    SQLiteDatabase db;
    Button btn_info_bluetooth, btn_info_brightness, btn_info_sound, btn_info_wifi, btn_info_airplane, btn_info_location;
    ProfileChanger profileChanger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      /*  Utils.onActivityCreateSetTheme(this);*/
        setContentView(R.layout.activity_home);
        /*---------------------*/
        spStore = new SharedPreferenceStore(getApplicationContext());
        dbHelper = new DbHelper(getApplicationContext());
        db = dbHelper.getWritableDatabase();
        /*--------------------------*/
        btn_info_bluetooth = (Button) findViewById(R.id.btn_info_bluetooth);
        btn_info_brightness = (Button) findViewById(R.id.btn_info_brightness);
        btn_info_sound = (Button) findViewById(R.id.btn_info_sound);
        btn_info_wifi = (Button) findViewById(R.id.btn_info_wifi);
        btn_info_airplane = (Button) findViewById(R.id.btn_info_airplane);
        btn_info_location = (Button) findViewById(R.id.btn_info_location);
        // core logic class to set and get profiles
        profileChanger = new ProfileChanger(getApplicationContext());
profileChanger.setScreenBrightness(255);
        // updating home widgets
        updateWidgets();
    }


    public void profiles(View v) {
        Toast.makeText(getApplicationContext(), "Profiles View", Toast.LENGTH_LONG).show();
    }

    public void navigation(View v) {
        switch (v.getId()) {
            case R.id.btn_new_profile:
                Toast.makeText(getApplicationContext(), "New Profile", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(), CreateProfileActivity.class));
                break;
            case R.id.btn_actions:
                startActivity(new Intent(getApplicationContext(), ActionsActivity.class));
                break;
        }

    }

    public void contactsList(View v) {
        Toast.makeText(getApplicationContext(), "Contacts", Toast.LENGTH_LONG).show();
    }

    public void settings(View v) {
        Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_LONG).show();
    }

    public void updateWidgets() {
        String bluetooth = profileChanger.getBlueToothState() ? "on" : "off";
        String wifi = profileChanger.getWifiState() ? "on" : "off";
        String airplane = profileChanger.getAirplaneMode() ? "on" : "off";
        int brightness = profileChanger.getScreenBrightness();
        int brightness_percentntage = (brightness / 255) * 100;
        btn_info_bluetooth.setText(bluetooth);
        btn_info_wifi.setText(wifi);
        btn_info_airplane.setText(airplane);
        btn_info_brightness.setText(brightness);
        Log.d("brightness",String.valueOf(brightness_percentntage));
    }


}
