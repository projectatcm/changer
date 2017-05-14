package com.codemagos.profilechanger.Receivers;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.codemagos.profilechanger.DbConnection.DbHelper;
import com.codemagos.profilechanger.ProfileChanger;

/**
 * Created by prasanth on 16/3/17.
 */

public class LocationReceiveService extends Service implements LocationListener {
    LocationManager locationManager;
    final static long minTime = 1 * 60 * 1000;
    final static float minDistance = 10;
    DbHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;
    @Override
    public void onCreate() {
        super.onCreate();
          /*-----------*/
        dbHelper = new DbHelper(getApplicationContext());
        sqLiteDatabase = dbHelper.getWritableDatabase();
        /*-----------*/
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {
        Toast.makeText(getApplicationContext(),location.getLatitude()+"",Toast.LENGTH_LONG).show();
        String latlng = location.getLatitude()+","+location.getLongitude();
        Cursor locationCursor = dbHelper.getLocation(sqLiteDatabase,latlng);
        if(locationCursor.moveToFirst()){
            String profile = locationCursor.getString(2);
            ProfileChanger profileChanger = new ProfileChanger(getApplicationContext());
            profileChanger.set(profile);
            Toast.makeText(getApplicationContext(),location.getLatitude()+profile,Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getApplicationContext(),"Not found",Toast.LENGTH_LONG).show();
        }
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
}
