package com.codemagos.profilechanger;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.codemagos.profilechanger.DbConnection.DbHelper;
import com.codemagos.profilechanger.Receivers.LocationReceiveService;

import java.util.ArrayList;

public class LocationSetActivity extends AppCompatActivity {
    Spinner spinner_profile;
    DbHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;
    Cursor profileCursor;
    Button btn_save, btn_location;
    String latlng,profile;
    LocationManager mLocationManager;
    ProgressDialog mProgessDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        setContentView(R.layout.activity_location_set);
         /*-----------*/
        dbHelper = new DbHelper(getApplicationContext());
        sqLiteDatabase = dbHelper.getWritableDatabase();
        profileCursor = dbHelper.getProfiles(sqLiteDatabase); // fetching profile's from database
        /*-----------*/
        mProgessDialog = new ProgressDialog(LocationSetActivity.this);
        btn_save = (Button) findViewById(R.id.btn_save);
        btn_location = (Button) findViewById(R.id.btn_location);
        spinner_profile = (Spinner) findViewById(R.id.spinner_profile);

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
        ArrayAdapter profileAdapter = new ArrayAdapter<String>(LocationSetActivity.this,
                R.layout.spinner_item, profiles_names);
        spinner_profile.setAdapter(profileAdapter);
        spinner_profile.setAdapter(profileAdapter);

        spinner_profile.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                profile = profiles_ids.get(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btn_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgessDialog.setMessage("Fetching Location...");
                mProgessDialog.show();
                if (ActivityCompat.checkSelfPermission(LocationSetActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(LocationSetActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 5000, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        mProgessDialog.hide();
                        latlng = location.getLongitude() + "," + location.getLatitude();
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                    }
                });
            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(profile.equals("") || profile.equals("")) {
                   Toast.makeText(getApplicationContext(),"Invalid",Toast.LENGTH_SHORT).show();
               }else{
                   dbHelper.addLocationAction(sqLiteDatabase,latlng,Integer.parseInt(profile));
                   startService(new Intent(getApplicationContext(),LocationReceiveService.class));
               }
            }
        });
    }
}
