package com.codemagos.profilechanger;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.support.design.widget.FloatingActionButton;
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
    FloatingActionButton float_btn_add_new;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiles);
        float_btn_add_new = (FloatingActionButton) findViewById(R.id.float_btn_add_new);
         /*---------------------*/
        spStore = new SharedPreferenceStore(getApplicationContext());
        dbHelper = new DbHelper(getApplicationContext());
        db = dbHelper.getWritableDatabase();

        /*--------------------------*/
        list_profiles = (ListView) findViewById(R.id.list_profiles);
        float_btn_add_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             startActivity(new Intent(getApplicationContext(),CreateProfileActivity.class));
                finish();
            }
        });
        updateProfileList();
    }

    public void updateProfileList(){
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
        list_profiles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(ProfilesActivity.this)
                        .setTitle("Delete")
                        .setMessage("Are you sure to delete")
                        // .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                dbHelper.deleteProfile(db,profileIDs.get(position).toString());
                                updateProfileList();
                            }})
                        .setNegativeButton(android.R.string.no, null).show();



            }
        });
    }
}
