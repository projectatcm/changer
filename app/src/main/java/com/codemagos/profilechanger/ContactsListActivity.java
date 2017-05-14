package com.codemagos.profilechanger;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.codemagos.profilechanger.Adapters.ContactsListAdapter;
import com.codemagos.profilechanger.DbConnection.DbHelper;
import com.codemagos.profilechanger.Spstore.SharedPreferenceStore;

import java.util.ArrayList;

public class ContactsListActivity extends AppCompatActivity {
    ListView list_contacts;
    SharedPreferenceStore spStore;
    DbHelper dbHelper;
    SQLiteDatabase db;
    FloatingActionButton float_btn_add_new;
    String name = "", number = "", ring = "RING";
    EditText txt_name, txt_number;
    Button btn_save;
    ImageButton btn_choose;
    Spinner spinner_ring;
    Dialog contactDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_list);
         /*---------------------*/
        spStore = new SharedPreferenceStore(getApplicationContext());
        dbHelper = new DbHelper(getApplicationContext());
        db = dbHelper.getWritableDatabase();
        /*--------------------------*/
        list_contacts = (ListView) findViewById(R.id.list_contacts);
        float_btn_add_new = (FloatingActionButton) findViewById(R.id.float_btn_add_new);
        contactDialog = new Dialog(ContactsListActivity.this);
        contactDialog.setContentView(R.layout.dialog_add_contact);
        contactDialog.setTitle("Add Contact");
        txt_name = (EditText) contactDialog.findViewById(R.id.txt_name);
        txt_number = (EditText) contactDialog.findViewById(R.id.txt_number);
        spinner_ring = (Spinner) contactDialog.findViewById(R.id.spinner_ring);
        btn_save = (Button) contactDialog.findViewById(R.id.btn_save);
        btn_choose = (ImageButton) contactDialog.findViewById(R.id.btn_choose);
        final String[] ring_modes = {"RING", "SILENT", "VIBRATE"};

        spinner_ring.setAdapter(new ArrayAdapter<String>(ContactsListActivity.this,
                R.layout.spinner_item, ring_modes));
        spinner_ring.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ring = ring_modes[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btn_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // importing contacts

            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // saving contact
                name = txt_name.getText().toString();
                number = txt_number.getText().toString();
                dbHelper.addContact(db, name, number, ring);
                contactDialog.hide();
                txt_name.setText("");
                txt_number.setText("");
                updateContacts();

            }
        });
        float_btn_add_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactDialog.show();
            }
        });
        updateContacts();
    }


    public void updateContacts() {
        Cursor contactsCursor = dbHelper.getContacts(db);
        final ArrayList ids = new ArrayList();
        ArrayList names = new ArrayList();
        ArrayList numbers = new ArrayList();
        ArrayList rings = new ArrayList();
        if (contactsCursor != null) {
            while (contactsCursor.moveToNext()) {
                String id = contactsCursor.getString(0);
                String name = contactsCursor.getString(1);
                String number = contactsCursor.getColumnName(2);
                String ring = contactsCursor.getString(3);
                ids.add(id);
                names.add(name);
                numbers.add(number);
                rings.add(ring);
            }

            ContactsListAdapter contactsListAdapter = new ContactsListAdapter(ContactsListActivity.this,ids, names, numbers, rings);
            list_contacts.setAdapter(contactsListAdapter);
            list_contacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                    new AlertDialog.Builder(ContactsListActivity.this)
                            .setTitle("Delete")
                            .setMessage("Are you sure to delete")
                           // .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dbHelper.deleteContacts(db,ids.get(position).toString());
                                    updateContacts();
                                }})
                            .setNegativeButton(android.R.string.no, null).show();



                }
            });
        }
    }
}
