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

    public DeskHelp(Context context) {
        super(context, "xyDesk.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String _ID = "_id INTEGER PRIMARY KEY AUTOINCREMENT,";
        db.execSQL("create table if not exists " + TABLE_Name + "(" +
                _ID +
                APP_NAME + " TEXT," +
                APP_PACKAGE_NAME + " TEXT," +
                APP_MAIN_ACTIVITY + " TEXT," +
                APP_POINT_PARENTS + " TEXT," +
                APP_POINT_ONE + " TEXT," +
                APP_POINT_TWO + " TEXT);");
        db.execSQL("create table if not exists " + TABLE_DELE_REC_NAME + "(" +
                _ID +
                DELE_APP_PACKAGE_NAME + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
