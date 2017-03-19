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

public class ContactsListAdapter extends ArrayAdapter {
    ArrayList names;
    ArrayList numbers;
    ArrayList rings;
    Activity activity;

    public ContactsListAdapter(Activity activity, ArrayList names, ArrayList numbers,ArrayList rings) {
        super(activity, R.layout.list_contact_row, names);
        this.activity = activity;
        this.names = names;
        this.numbers = numbers;
        this.rings = rings;

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        View rowView = layoutInflater.inflate(R.layout.list_contact_row, null);

        TextView txt_name = (TextView) rowView.findViewById(R.id.txt_name);
        TextView txt_number = (TextView) rowView.findViewById(R.id.txt_number);
        Button btn_ring = (Button) rowView.findViewById(R.id.btn_ring);

        String name = names.get(position).toString();
        String number = numbers.get(position).toString();
        String ring =rings.get(position).toString();

        txt_name.setText(name);
        txt_number.setText(number);
        btn_ring.setText(ring);



        return rowView;

    }
}
