package com.codemagos.profilechanger.Spstore;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.codemagos.profilechanger.Utils.Modes;

import java.util.HashMap;
import java.util.Map;


public class SharedPreferenceStore {
	public final static String SHARED_PREFS = "PreferenceStore";
	private Editor edit;
	private SharedPreferences settings;
	public SharedPreferenceStore(Context context){
		settings = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
	}
	public void setPrevRingMode(int mode){
		edit = settings.edit();
		edit.putInt("MODE", mode);
		edit.commit();
	}

	public int getPrevRingMode(){
		return settings.getInt("MODE", 0);
	}

	public boolean isOnDefault(){
		// if default values are set returns true else returns false
		return settings.getBoolean("default_set", false);
	}

	public void setBackupPhone(String number){
		edit = settings.edit();
		edit.putString("number", number);
		edit.commit();
	}
	public String getBackupPhone(){
		return settings.getString("number","");
	}

}
