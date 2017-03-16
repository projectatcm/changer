package com.codemagos.profilechanger;

import android.app.Dialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.codemagos.profilechanger.DbConnection.DbHelper;
import com.codemagos.profilechanger.Spstore.SharedPreferenceStore;
import com.codemagos.profilechanger.Utils.Alert;

import java.util.ArrayList;
import java.util.List;

public class CreateProfileActivity extends AppCompatActivity {
    SharedPreferenceStore spStore;
    DbHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;
    EditText txt_profile_name;
    Switch switch_wifi, switch_bluetooth;
Spinner spinner_ring;
    SeekBar seekBar_brightness;
    Button btn_save;

    // String variables to save settings
    String profileName, wifiName, brightness = "", wifi = "", bluetooth = "", airplane, mobile_data, ring, action;


    String[] ring_modes = {"RING", "SILENT", "VIBRATE"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);
        getSupportActionBar().setHomeButtonEnabled(true);
        /*------------------*/
        spStore = new SharedPreferenceStore(getApplicationContext());
        dbHelper = new DbHelper(getApplicationContext());
        sqLiteDatabase = dbHelper.getWritableDatabase();
        /*------------------*/
        txt_profile_name = (EditText) findViewById(R.id.txt_profile_name);
        switch_wifi = (Switch) findViewById(R.id.switch_wifi);
        switch_bluetooth = (Switch) findViewById(R.id.switch_bluetooth);
        seekBar_brightness = (SeekBar) findViewById(R.id.seekBar_brightness);
        spinner_ring = (Spinner) findViewById(R.id.spinner_ring);
        spinner_ring.setAdapter(new ArrayAdapter<String>(this,
                R.layout.spinner_item, ring_modes));
        btn_save = (Button) findViewById(R.id.btn_save);
        action = "";
        spinner_ring.setOnItemSelectedListener(ringSelect);

        /*------------------*/

        seekBar_brightness.setOnSeekBarChangeListener(brightnessChange);

        btn_save.setOnClickListener(saveAction);
        switch_wifi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    wifi = "on";
                } else {
                    wifi = "off";
                }
            }
        });
        switch_bluetooth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    bluetooth = "on";
                } else {
                    bluetooth = "off";
                }
            }
        });
    }

    // getting data from brightness selector
    SeekBar.OnSeekBarChangeListener brightnessChange = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            brightness = String.valueOf(progress);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    AdapterView.OnItemSelectedListener ringSelect = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            ring = ring_modes[position];
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    // save button click action 
    View.OnClickListener saveAction = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            profileName = txt_profile_name.getText().toString();
            Log.d("RING", ring);
            Log.d("BRIGHTNESS", brightness);
            Log.d("WIFI", wifi);
            Log.d("BLUETOOTH", bluetooth);
            Log.d("PROFILE_NAME", profileName);
                Long profileID = dbHelper.addProfile(sqLiteDatabase, profileName, "", bluetooth, brightness, "", "", ring, wifi);
                Toast.makeText(getApplicationContext(), "Profile Added : " + profileID, Toast.LENGTH_LONG).show();

        }
    };

}
