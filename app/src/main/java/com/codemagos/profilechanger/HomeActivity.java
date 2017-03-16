package com.codemagos.profilechanger;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.codemagos.profilechanger.DbConnection.DbHelper;
import com.codemagos.profilechanger.Spstore.SharedPreferenceStore;
import com.codemagos.profilechanger.Utils.Utils;

public class HomeActivity extends AppCompatActivity {
    SharedPreferenceStore spStore;
    DbHelper dbHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      /*  Utils.onActivityCreateSetTheme(this);*/
        setContentView(R.layout.activity_home);
        spStore = new SharedPreferenceStore(getApplicationContext());
        dbHelper = new DbHelper(getApplicationContext());
        db = dbHelper.getWritableDatabase();
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

}
