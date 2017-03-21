package com.codemagos.profilechanger;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.codemagos.profilechanger.Adapters.ProfilesListAdapter;
import com.codemagos.profilechanger.DbConnection.DbHelper;
import com.codemagos.profilechanger.Spstore.SharedPreferenceStore;

import java.util.ArrayList;

public class ProfilesActivity extends AppCompatActivity {
    SharedPreferenceStore spStore;
    DbHelper dbHelper;
    SQLiteDatabase db;
    ListView list_profiles;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiles);
         /*---------------------*/
        spStore = new SharedPreferenceStore(getApplicationContext());
        dbHelper = new DbHelper(getApplicationContext());
        db = dbHelper.getWritableDatabase();

        /*--------------------------*/
        list_profiles = (ListView) findViewById(R.id.list_profiles);
        final ArrayList profilesNames = new ArrayList();
        final ArrayList profileIDs = new ArrayList();
        Cursor profilesCursor = dbHelper.getProfiles(db);
        while(profilesCursor.moveToNext()){
            String id = profilesCursor.getString(0);
            String name = profilesCursor.getString(1);
            profileIDs.add(id);
            profilesNames.add(name);
        }

        ProfilesListAdapter profilesListAdapter = new ProfilesListAdapter(ProfilesActivity.this,profilesNames);
        list_profiles.setAdapter(profilesListAdapter);
        list_profiles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),profilesNames.get(position).toString(),Toast.LENGTH_LONG).show();
            }
        });

    }
}
