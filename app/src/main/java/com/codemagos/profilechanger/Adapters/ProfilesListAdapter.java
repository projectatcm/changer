package com.codemagos.profilechanger.Adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.codemagos.profilechanger.R;

import java.util.ArrayList;

/**
 * Created by prasanth on 19/3/17.
 */

public class ProfilesListAdapter extends ArrayAdapter {
    ArrayList profiels;
    Activity activity;

    public ProfilesListAdapter(Activity activity, ArrayList profiels) {
        super(activity, R.layout.list_profiles, profiels);
        this.activity = activity;
this.profiels = profiels;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        View rowView = layoutInflater.inflate(R.layout.list_profiles, null);

        TextView txt_name = (TextView) rowView.findViewById(R.id.txt_name);

        String name = profiels.get(position).toString();

        txt_name.setText(name);

        return rowView;

    }


}
