package xydesk.xy.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import xydesk.xy.model.XYAppInfoInDesk;

/**
 * Created by haizeiym
 * on 2016/8/8
 */
public class DeskDB {
    DeskHelp deskHelp;

    public DeskDB(Context context) {
        deskHelp = new DeskHelp(context);
    }

    //添加app信息
    public void addAppInfo(XYAppInfoInDesk xyAppInfoInDesk) {
        SQLiteDatabase sd = deskHelp.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(deskHelp.APP_PACKAGE_NAME, xyAppInfoInDesk.appPackageName);
        cv.put(deskHelp.APP_NAME, xyAppInfoInDesk.appName);
        cv.put(deskHelp.APP_MAIN_ACTIVITY, xyAppInfoInDesk.appMainActivity);
        cv.put(deskHelp.APP_POINT_PARENTS, xyAppInfoInDesk.appPonitParents);
        cv.put(deskHelp.APP_POINT_ONE, xyAppInfoInDesk.appPonitOne);
        cv.put(deskHelp.APP_POINT_TWO, xyAppInfoInDesk.appPonitTwo);
        sd.insert(deskHelp.TABLE_Name, null, cv);
    }

    //删除屏幕上app
    public void deleApp(String appPackage) {
        SQLiteDatabase sd = deskHelp.getWritableDatabase();
        sd.delete(deskHelp.TABLE_Name, deskHelp.APP_PACKAGE_NAME + "=?", new String[]{appPackage});
    }

    //修改app名称
    public void updateAppName(String newName) {
        SQLiteDatabase sd = deskHelp.getWritableDatabase();
        sd.execSQL("update " + deskHelp.TABLE_Name + " set " + "where " + deskHelp.APP_NAME + "=?", new String[]{newName});
    }

    //根据pointParents获取所有app信息
    public List<XYAppInfoInDesk> getAllApp(String appPonitParents) {
        List<XYAppInfoInDesk> xyAppInfoInDesks = new ArrayList<>();
        SQLiteDatabase sd = null;
        Cursor cs = null;
        try {
            sd = deskHelp.getReadableDatabase();
            cs = sd.rawQuery("select * from " + deskHelp.TABLE_Name + " where " + deskHelp.APP_POINT_PARENTS + "=?", new String[]{appPonitParents});
            if (cs != null) {
                while (cs.moveToNext()) {
                    XYAppInfoInDesk xyAppInfoInDesk = new XYAppInfoInDesk();
                    xyAppInfoInDesk.appName = getString(cs, deskHelp.APP_NAME);
                    xyAppInfoInDesk.appPackageName = getString(cs, deskHelp.APP_PACKAGE_NAME);
                    xyAppInfoInDesk.appMainActivity = getString(cs, deskHelp.APP_MAIN_ACTIVITY);
                    xyAppInfoInDesk.appPonitParents = getString(cs, deskHelp.APP_POINT_PARENTS);
                    xyAppInfoInDesk.appPonitOne = getString(cs, deskHelp.APP_POINT_ONE);
                    xyAppInfoInDesk.appPonitTwo = getString(cs, deskHelp.APP_POINT_TWO);
                    xyAppInfoInDesks.add(xyAppInfoInDesk);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cs != null) {
                cs.close();
            }
            if (sd != null) {
                sd.close();
            }
        }
        return xyAppInfoInDesks;
    }

    private String getString(Cursor cs, String index) {
        return cs.getString(cs.getColumnIndex(index));
    }
}
