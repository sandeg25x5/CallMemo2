package com.example.sandeeplamsal123.testapplication2.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

import com.example.sandeeplamsal123.testapplication2.models.ContactModel;

import java.util.ArrayList;
import java.util.List;

public class ContactsUtils {

    Cursor cursor;
    Context context;
    private static String TAG = ContactsUtils.class.getSimpleName();

    public ContactsUtils(Context context) {
        this.context = context;
    }

    public static List<ContactModel> getContactsDetails(String name, String number) {
        List<ContactModel> contactModels = new ArrayList<>();
        ContactModel model = new ContactModel(name, number);
        contactModels.add(model);
        return contactModels;
    }

//    public void getContactNameAndNumber(String name,String number) {
//        String name = ContactsContract.CommonDataKinds.StructuredName.DATA1;
//        Integer number = ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE;
//        String mobile = String.valueOf(number);
//        return name;
//    }

    public List<ContactModel> getContactList() {
        List<ContactModel> models = new ArrayList<>();
        ContactModel model;
        ContentResolver cr = context.getContentResolver();

        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));

                if (cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                        Log.i(TAG, "Name: " + name);
                        Log.i(TAG, "Phone Number: " + phoneNo);
                        model = new ContactModel(name, phoneNo);
                        models.add(model);
                    }
                    pCur.close();
                }

            }
            //

        }
        if (cur != null) {
            cur.close();
        }
        return models;
    }

}
