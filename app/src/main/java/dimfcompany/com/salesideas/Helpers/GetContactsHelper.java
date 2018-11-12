package dimfcompany.com.salesideas.Helpers;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.LongSparseArray;

import java.util.LinkedList;
import java.util.List;

import dimfcompany.com.salesideas.Models.Model_Phone_Contact;

public class GetContactsHelper
{
    public static List<Model_Phone_Contact> getAll(Context ctx)
    {
        List<Model_Phone_Contact> list = new LinkedList<Model_Phone_Contact>();
        LongSparseArray<Model_Phone_Contact> array = new LongSparseArray<Model_Phone_Contact>();
        long start = System.currentTimeMillis();

        String[] projection = {
                ContactsContract.Data.MIMETYPE,
                ContactsContract.Data.CONTACT_ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Contactables.DATA,
                ContactsContract.CommonDataKinds.Contactables.TYPE,
        };
        String selection = ContactsContract.Data.MIMETYPE + " in (?, ?)";
        String[] selectionArgs = {
                ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,
        };
        String sortOrder = ContactsContract.Contacts.SORT_KEY_ALTERNATIVE;

        Uri uri = ContactsContract.CommonDataKinds.Contactables.CONTENT_URI;
        // we could also use Uri uri = ContactsContract.Data.CONTENT_URI;

        // ok, let's work...
        Cursor cursor = ctx.getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);

        final int mimeTypeIdx = cursor.getColumnIndex(ContactsContract.Data.MIMETYPE);
        final int idIdx = cursor.getColumnIndex(ContactsContract.Data.CONTACT_ID);
        final int nameIdx = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
        final int dataIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Contactables.DATA);
        final int typeIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Contactables.TYPE);

        while (cursor.moveToNext()) {
            long id = cursor.getLong(idIdx);
            Model_Phone_Contact addressBookContact = array.get(id);
            if (addressBookContact == null)
            {
                addressBookContact = new Model_Phone_Contact(id, cursor.getString(nameIdx));
                array.put(id, addressBookContact);
                list.add(addressBookContact);
            }
            int type = cursor.getInt(typeIdx);
            String data = cursor.getString(dataIdx);
            String mimeType = cursor.getString(mimeTypeIdx);
            if (mimeType.equals(ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)) {
                // mimeType == ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE
                addressBookContact.addEmail(type, data);
            } else {
                // mimeType == ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
                addressBookContact.addPhone(type, data);
            }
        }
        long ms = System.currentTimeMillis() - start;
        cursor.close();

        return list;
    }
}
