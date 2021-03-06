package com.codemagos.profilechanger.DbConnection;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by prasanth on 8/3/17.
 */

public class DbHelper extends SQLiteOpenHelper {
    final static String DB_NAME = "CHANGER.DB";
    final static int DB_VERSION = 1;
    static SQLiteDatabase DB;
    Context context;
    String LOG = "Changer DB -> ";
    String CREATE_PROFILE_TABLE = "CREATE TABLE IF NOT EXISTS profile(id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "name TEXT," +
            "airplane TEXT," +
            "bluetooth TEXT," +
            "brightness TEXT," +
            "gps TEXT," +
            "mobile_data TEXT," +
            "ring TEXT," +
            "wifi TEXT" +
            ");";
    String CREATE_CONTACTS_TABLE = "CREATE TABLE IF NOT EXISTS contacts(id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "name TEXT," +
            "number TEXT," +
            "ring TEXT" +
            ");";

    String CREATE_WIFI_ACTION = "CREATE TABLE IF NOT EXISTS wifi_action(id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "ssid TEXT," +
            "profile_on_connect INTEGER," +
            "profile_on_disconnect INTEGER," +
            "status INTEGER" +
            ");";
    String CREATE_LOCATION_ACTION = "CREATE TABLE IF NOT EXISTS location_action(id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "latlng TEXT," +
            "profile INTEGER"+
            ");";
    String CREATE_ALARM_ACTION = "CREATE TABLE IF NOT EXISTS alarm_action(id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "time DATETIME," +
            "profile_on_trigger INTEGER" +
            ");";


    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        Log.e("DATABASE OPERATIONS", "Database Created / Opened :-)");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating tables
        db.execSQL(CREATE_PROFILE_TABLE);
      db.execSQL(CREATE_ALARM_ACTION);
        db.execSQL(CREATE_WIFI_ACTION);
        db.execSQL(CREATE_CONTACTS_TABLE);
        db.execSQL(CREATE_LOCATION_ACTION);
        Log.i(LOG,"Tables created !");
    }

    public void addLocationAction(SQLiteDatabase db,String latlng,int profile){
        ContentValues contentValues = new ContentValues();
        contentValues.put("latlng", latlng);
        contentValues.put("profile", profile);
        db.insert("location_action", null, contentValues);
        Log.i(LOG,"Location Action Added");
    }


    public void addWifiAction(SQLiteDatabase db,String ssid,int profile_on_connect,int profile_on_disconnect ){
        ContentValues contentValues = new ContentValues();
        contentValues.put("ssid", ssid);
        contentValues.put("profile_on_connect", profile_on_connect);
        contentValues.put("profile_on_disconnect", profile_on_disconnect);
        contentValues.put("status", 1);
        db.insert("wifi_action", null, contentValues);
        Log.i(LOG,"Wifi Action Added");
    }

    public Long addProfile(SQLiteDatabase db,String name,String airplane,String bluetooth,
                           String brightness,String gps,String mobile_data,
                           String ring,String wifi){
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("airplane", airplane);
        contentValues.put("bluetooth", bluetooth);
        contentValues.put("brightness", brightness);
        contentValues.put("gps", gps);
        contentValues.put("mobile_data", mobile_data);
        contentValues.put("ring", ring);
        contentValues.put("wifi", wifi);
        // inserting into table
       Long last_insert_id =  db.insert("profile", null, contentValues);
        Log.i(LOG,"Profile Added");
        return last_insert_id;
    }
    public Long addContact(SQLiteDatabase db,String name,String number,String ring){
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("number", number);
        contentValues.put("ring", ring);
        Long last_insert_id =  db.insert("contacts", null, contentValues);
        Log.i(LOG,"contact Added");
        return last_insert_id;
    }
    public Cursor getContacts(SQLiteDatabase db){
        String query = "Select * from contacts";
        String[] params = null;
        Cursor rs = db.rawQuery(query,params);
        return rs;
    }
    public void deleteContacts(SQLiteDatabase db,String id){
        db.execSQL("delete from contacts where id='"+id+"'");
    }
    public void deleteProfile(SQLiteDatabase db,String id){
        db.execSQL("delete from profile where id='"+id+"'");
    }
    public Cursor searchInContact(SQLiteDatabase db,String number){
        String query = "Select * from contacts where number = '"+number+"'";
        String[] params = null;
        Cursor rs = db.rawQuery(query,params);
        return rs;
    }
    public Cursor getContact(SQLiteDatabase db,String contactID){
        String query = "Select * from contacts where id = '"+contactID+"'";
        String[] params = null;
        Cursor rs = db.rawQuery(query,params);
        return rs;
    }
    public Cursor getLocation(SQLiteDatabase db,String latlng){
        String query = "Select * from location_action";
        String[] params = null;
        Cursor rs = db.rawQuery(query,params);
        return rs;
    }
    public Cursor getProfiles(SQLiteDatabase db){
        String query = "Select * from profile";
        String[] params = null;
        Cursor rs = db.rawQuery(query,params);
        return rs;
    }
    public Cursor getProfile(SQLiteDatabase db,String id){
        String query = "Select * from profile where id = '"+id+"'";
        String[] params = null;
        Cursor rs = db.rawQuery(query,params);
        return rs;
    }

    public Cursor getWifiAction(SQLiteDatabase db,String ssid){
        String query = "Select * from wifi_action where ssid = '"+ssid+"'";
        String[] params = null;
        Cursor rs = db.rawQuery(query,params);
        return rs;
    }








    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
