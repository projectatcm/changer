package com.codemagos.profilechanger;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.codemagos.profilechanger.DbConnection.DbHelper;
import com.codemagos.profilechanger.Receivers.AlarmReceiver;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class ActionAlarmSetActivity extends AppCompatActivity {
EditText txt_time_from,txt_time_to;
    Button btn_save;
    Spinner spinner_profile_on,spinner_profile_off;
    String time_from,time_to,profile_on,profile_off;
    Long mTimeFrom,mTimeTo;
    DbHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;
    Cursor profileCursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_alarm_set);
         /*-----------*/
        dbHelper = new DbHelper(getApplicationContext());
        sqLiteDatabase = dbHelper.getWritableDatabase();
        profileCursor = dbHelper.getProfiles(sqLiteDatabase); // fetching profile's from database
        /*-----------*/
        txt_time_from = (EditText) findViewById(R.id.txt_time_from);
        txt_time_to = (EditText) findViewById(R.id.txt_time_to);
        btn_save = (Button) findViewById(R.id.btn_save);
        spinner_profile_on = (Spinner) findViewById(R.id.spinner_profile_on);
        spinner_profile_off  =(Spinner) findViewById(R.id.spinner_profile_off);

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
        ArrayAdapter profileAdapter = new ArrayAdapter<String>(ActionAlarmSetActivity.this,
                R.layout.spinner_item, profiles_names);
        spinner_profile_on.setAdapter(profileAdapter);
        spinner_profile_off.setAdapter(profileAdapter);

        spinner_profile_on.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                profile_on = profiles_ids.get(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_profile_off.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                profile_off = profiles_ids.get(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        txt_time_from.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(ActionAlarmSetActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        txt_time_from.setText( selectedHour + ":" + selectedMinute);
                        Calendar c2 = Calendar.getInstance();
                        c2.set(Calendar.HOUR_OF_DAY, selectedHour);
                        c2.set(Calendar.MINUTE, selectedMinute);
                        long sub = c2.getTimeInMillis() - mcurrentTime.getTimeInMillis();
                        mTimeFrom = sub;
                        Toast.makeText(getApplicationContext(),sub+"",Toast.LENGTH_LONG).show();
                    }
                }, hour, minute, false);//12 hour time
                mTimePicker.setTitle("Select Time");

                mTimePicker.show();
            }
        });
        txt_time_to.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(ActionAlarmSetActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        txt_time_to.setText( selectedHour + ":" + selectedMinute);
                        Calendar c2 = Calendar.getInstance();
                        c2.set(Calendar.HOUR_OF_DAY, selectedHour);
                        c2.set(Calendar.MINUTE, selectedMinute);
                        long sub = c2.getTimeInMillis() - mcurrentTime.getTimeInMillis();
                        mTimeTo = sub;
                        Toast.makeText(getApplicationContext(),sub+"",Toast.LENGTH_LONG).show();
                    }
                }, hour, minute, false);//12 hour time
                mTimePicker.setTitle("Select Time");

                mTimePicker.show();
            }
        });
btn_save.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        time_from = txt_time_from.getText().toString();
        time_to = txt_time_to.getText().toString();
        setAlarm(mTimeFrom,profile_on);
        setAlarm(mTimeTo,profile_off);
    }
});

    }
    public void setAlarm(long time,String profileID){
        Long alerttime = new GregorianCalendar().getTimeInMillis() + 5*1000; // 5 sec
        Intent alertIntent = new Intent(getApplicationContext(), AlarmReceiver.class);

        alertIntent.putExtra("profileID",profileID);

        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP,alerttime,PendingIntent.getBroadcast(this,1,alertIntent,PendingIntent.FLAG_UPDATE_CURRENT));



    }

}
