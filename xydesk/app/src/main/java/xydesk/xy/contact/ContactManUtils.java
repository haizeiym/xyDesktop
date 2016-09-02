package xydesk.xy.contact;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import java.util.HashMap;

/**
 * Created by haizeiym
 * on 2016/9/1
 */
public class ContactManUtils {
    public static HashMap<String, String> allContact = new HashMap<>();

    //获取手机联系人号码
    public static void getPeopleInPhone(Context context) {
        allContact.clear();
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);     // 获取手机联系人
            while (cursor != null && cursor.moveToNext()) {
                int indexPeopleName = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);    // people name
                int indexPhoneNum = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);            // phone number
                String strPeopleName = cursor.getString(indexPeopleName);
                String strPhoneNum = cursor.getString(indexPhoneNum);
                allContact.put(strPeopleName, strPhoneNum);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    //拨打电话
    public static void callPhone(Activity activity, String num) {
        Intent callIntent = new Intent();
        callIntent.setAction("android.intent.action.CALL");
        callIntent.addCategory("android.intent.category.DEFAULT");
        callIntent.setData(Uri.parse("tel:" + num));
        activity.startActivity(callIntent);
    }
}
