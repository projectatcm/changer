package com.codemagos.profilechanger;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.Spinner;
import android.widget.Toast;

import com.codemagos.profilechanger.DbConnection.DbHelper;

import java.util.ArrayList;
import java.util.List;

public class ActionWifiSetActivity extends AppCompatActivity {
    Spinner spinner_wifi, spinner_profile_on_connect, spinner_profile_on_disconnect;
    DbHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;
    Cursor profileCursor;
    Button btn_save;
    String ssid="";
    int profile_on=0,profile_off=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_wifi_set);
        /*-----------*/
        dbHelper = new DbHelper(getApplicationContext());
        sqLiteDatabase = dbHelper.getWritableDatabase();
        profileCursor = dbHelper.getProfiles(sqLiteDatabase); // fetching profile's from database
        /*-----------*/
        spinner_wifi = (Spinner) findViewById(R.id.spinner_wifi);
        spinner_profile_on_connect = (Spinner) findViewById(R.id.spinner_profile_on_connect);
        spinner_profile_on_disconnect = (Spinner) findViewById(R.id.spinner_profile_on_disconnect);
        btn_save = (Button) findViewById(R.id.btn_save);

       /*------------------------*/
        // Adding wifi names in wifi spinner
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        List wifiList = wifiManager.getConfiguredNetworks();
        final ArrayList wifis = new ArrayList<String>();
        List<WifiConfiguration> configs = wifiManager.getConfiguredNetworks();
        for (WifiConfiguration config : configs) {
            String SSID = config.SSID;
            if (SSID.startsWith("\"") && SSID.endsWith("\"")) {
                // to remove aditional quotes in SSID
                SSID = SSID.substring(1, SSID.length() - 1);
            }
            wifis.add(SSID);
        }
        Log.e("WIFI", wifis.toString());
        // setting wifi names into android spinner
        ArrayAdapter wifiArrayAdapter = new ArrayAdapter<String>(ActionWifiSetActivity.this,
                R.layout.spinner_item, wifis);
        spinner_wifi.setAdapter(wifiArrayAdapter);
        spinner_wifi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ssid =  wifis.get(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        /*------------------------*/
        // Adding profiles names in wifi spinner
        final ArrayList profiles_names = new ArrayList();
        final ArrayList profiles_ids = new ArrayList();

        while (profileCursor.moveToNext()){
            String profileID = profileCursor.getString(0);
            String profileName = profileCursor.getString(1);
            profiles_names.add(profileName);
            profiles_ids.add(profileID);
        }
        if(profiles_ids.size() == 0){
            Toast.makeText(getApplicationContext(),"Add Profiles First",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(),CreateProfileActivity.class));
            finish();
        }
        ArrayAdapter profileAdapter = new ArrayAdapter<String>(ActionWifiSetActivity.this,
                R.layout.spinner_item, profiles_names);
        spinner_profile_on_connect.setAdapter(profileAdapter);
        spinner_profile_on_disconnect.setAdapter(profileAdapter);
        // creating spinner profile select listener
        spinner_profile_on_connect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               profile_on = Integer.valueOf(profiles_ids.get(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_profile_on_disconnect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                profile_off = Integer.valueOf(profiles_ids.get(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ssid.equals("")){
                    Toast.makeText(getApplicationContext(),"",Toast.LENGTH_SHORT).show();
                }else if(profile_on == 0){
                    Toast.makeText(getApplicationContext(),"",Toast.LENGTH_SHORT).show();
                }else if(profile_off == 0){
                    Toast.makeText(getApplicationContext(),"",Toast.LENGTH_SHORT).show();
                }else if(profile_on == profile_off){
                    Toast.makeText(getApplicationContext(),"",Toast.LENGTH_SHORT).show();
                }else{
                    dbHelper.addWifiAction(sqLiteDatabase,ssid,profile_on,profile_off);
                }
            }
        });
    }

}
