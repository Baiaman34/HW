package com.example.hw;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.hw.Contacts;
import com.example.hw.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static  boolean READ_CONTACTS_GRANTED = false;
    private static final int REQUEST_CODE_READ_CONTACTS = 1;
    private ListView list12;
    private ArrayList<String> contactsName = new ArrayList<>();
    private ArrayList<Contacts> contact = new ArrayList<>();
    private String contactName;
    private String contactNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int hasReadContactPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
        if (hasReadContactPermission == PackageManager.PERMISSION_GRANTED) {
            READ_CONTACTS_GRANTED = true;
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CODE_READ_CONTACTS);
        }
        if (READ_CONTACTS_GRANTED) {
            loadContact();
        }
    }

    @SuppressLint("Range")
    private void loadContact() {
        ContentResolver contentResolver = getContentResolver();
        Cursor cursorName = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if (cursorName != null){

            while(cursorName.moveToNext()){
                contactName = cursorName.getString(cursorName.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY));

                if (Integer.parseInt(cursorName.getString(cursorName.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)))>0){
                    String[] ids = {cursorName.getString(cursorName.getColumnIndex(ContactsContract.Contacts._ID))};
                    Cursor cursorNum = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "  = ?"
                            ,  ids, null);
                    while (cursorNum.moveToNext()) {
                        contactNum = cursorNum.getString(cursorNum.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    }
                    cursorNum.close();
                    Contacts contactModel = new Contacts(contactName, contactNum);
                    contact.add(contactModel);
                }
            }
            cursorName.close();
        }

        for (Contacts name : contact) {
            contactsName.add(name.getContactName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, contactsName);
        list12 = findViewById(R.id.listview_contacts);
        list12.setAdapter(adapter);

        list12.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", contact.get(position).getPhoneNumbers(), null));
                startActivity(intent);

            }
        });
    }
}