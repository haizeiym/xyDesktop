package xydesk.xy.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by haizeiym
 * on 2016/8/8
 */
public class DeskHelp extends SQLiteOpenHelper {
    //主屏信息
    public final String TABLE_Name = "xydesk";
    public final String APP_NAME = "appName";
    public final String APP_PACKAGE_NAME = "appPackageName";
    public final String APP_MAIN_ACTIVITY = "appMainActivity";
    public final String APP_POINT_PARENTS = "appPointParents";
    public final String APP_POINT_ONE = "appPointOne";
    public final String APP_POINT_TWO = "appPointTwo";

    //已删除记录
    public final String TABLE_DELE_REC_NAME = "dele_rec_name";
    public final String DELE_APP_PACKAGE_NAME = "dele_app_package_name";

    //语音查找应用设置
    public final String TABLE_APP_NAME_SET_NAME = "table_app_name_set_name";
    public final String APP_SET_NAME = "app_set_name";
    public final String APP_SET_PACKAGE_NAME = "app_set_package_name";
    //语音查找联系人设置
    public final String TABLE_CONTACT_NAME_SET_NAME = "table_contact_name_set_name";
    public final String CONTACT_NAME = "contact_name";
    public final String CONTACT_NUMBER = "contact_number";

    public DeskHelp(Context context) {
        super(context, "xyDesk.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE = "create table if not exists ";
        String _ID = "_id INTEGER PRIMARY KEY AUTOINCREMENT,";
        String TEXT = " TEXT,";
        String TEXT_ = " TEXT);";
        db.execSQL(CREATE + TABLE_Name + " (" +
                _ID +
                APP_NAME + TEXT +
                APP_PACKAGE_NAME + TEXT +
                APP_MAIN_ACTIVITY + TEXT +
                APP_POINT_PARENTS + TEXT +
                APP_POINT_ONE + TEXT +
                APP_POINT_TWO + TEXT_);
        db.execSQL(CREATE + TABLE_DELE_REC_NAME + " (" +
                _ID +
                DELE_APP_PACKAGE_NAME + TEXT_);
        //语音APP名称设置
        db.execSQL(CREATE + TABLE_APP_NAME_SET_NAME + " (" +
                _ID +
                APP_SET_NAME + TEXT +
                APP_SET_PACKAGE_NAME + TEXT_);
        //联系人
        db.execSQL(CREATE + TABLE_CONTACT_NAME_SET_NAME + " (" +
                _ID +
                CONTACT_NAME + TEXT +
                CONTACT_NUMBER + TEXT_);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
