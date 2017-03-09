package com.codemagos.profilechanger.Spstore;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.codemagos.profilechanger.Utils.Modes;


public class SharedPreferenceStore {
	public final static String SHARED_PREFS = "PreferenceStore";
	private Editor edit;
	private SharedPreferences settings;
	public SharedPreferenceStore(Context context){
		settings = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
	}
	public void setMode(int mode){
		edit = settings.edit();
		edit.putInt("MODE", mode);
		edit.commit();
	}

	public int getMode(){
		return settings.getInt("MODE", Modes.OFF);
	}




}
