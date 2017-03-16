package com.codemagos.profilechanger.Utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by prasanth on 10/3/17.
 */

public class Alert {

    public static void showShortToast(Context context,String message){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }
    public static void showLongToast(Context context,String message){
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }
}
