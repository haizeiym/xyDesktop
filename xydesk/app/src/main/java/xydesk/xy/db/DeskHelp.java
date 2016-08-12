package xydesk.xy.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by haizeiym
 * on 2016/8/8
 */
public class DeskHelp extends SQLiteOpenHelper {
    public final String TABLE_Name = "xydesk";
    public final String APP_NAME = "appName";
    public final String APP_PACKAGE_NAME = "appPackageName";
    public final String APP_MAIN_ACTIVITY = "appMainActivity";
    public final String APP_POINT_PARENTS = "appPointParents";
    public final String APP_POINT_ONE = "appPointOne";
    public final String APP_POINT_TWO = "appPointTwo";

    public DeskHelp(Context context) {
        super(context, "xyDesk.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists " + TABLE_Name + "(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                APP_NAME + " TEXT," +
                APP_PACKAGE_NAME + " TEXT," +
                APP_MAIN_ACTIVITY + " TEXT," +
                APP_POINT_PARENTS + " TEXT," +
                APP_POINT_ONE + " TEXT," +
                APP_POINT_TWO + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
