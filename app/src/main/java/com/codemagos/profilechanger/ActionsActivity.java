package com.codemagos.profilechanger;

import android.app.Dialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ActionsActivity extends AppCompatActivity {
FloatingActionButton float_btn_add_new;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actions);
        float_btn_add_new = (FloatingActionButton) findViewById(R.id.float_btn_add_new);
        float_btn_add_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(ActionsActivity.this);
                dialog.setContentView(R.layout.dialog_action_select);
                Button btn_wifi = (Button) dialog.findViewById(R.id.btn_wifi);
                Button btn_call = (Button) dialog.findViewById(R.id.btn_call);
                Button btn_alarm = (Button) dialog.findViewById(R.id.btn_alarm);
                Button btn_location = (Button) dialog.findViewById(R.id.btn_location);
                btn_wifi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getApplicationContext(),ActionWifiSetActivity.class));
                    }
                });
                btn_call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getApplicationContext(),ActionWifiSetActivity.class));
                    }
                });
                dialog.setTitle("Choose Action Type");
                dialog.show();
            }
        });
    }


}
