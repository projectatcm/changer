package com.codemagos.profilechanger;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by prasanth on 3/4/17.
 */

public class DialogActivity extends Activity{
    Intent intent;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dilaog_mode_changer);
        intent = getIntent();
        String text = intent.getStringExtra("text");
        int duration = intent.getIntExtra("duration",2000);
        int icon = intent.getIntExtra("icon",R.drawable.ic_profiles);
        ImageView img_icon = (ImageView) findViewById(R.id.img_icon);
        TextView txt_text = (TextView) findViewById(R.id.txt_text);
        img_icon.setImageResource(icon);
        txt_text.setText(text);
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                       finish();
                    }
                }, duration);
    }
}
