package xydesk.xy.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import xydesk.xy.MyApplication;
import xydesk.xy.contant.XYContant;
import xydesk.xy.model.XYAppInfoInDesk;
import xydesk.xy.model.XYXFNameSetModel;
import xydesk.xy.utils.Utils;

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
        if (!isExits(xyAppInfoInDesk.appPackageName)) {
            ContentValues cv = new ContentValues();
            cv.put(deskHelp.APP_PACKAGE_NAME, xyAppInfoInDesk.appPackageName);
            cv.put(deskHelp.APP_NAME, xyAppInfoInDesk.appName);
            cv.put(deskHelp.APP_MAIN_ACTIVITY, xyAppInfoInDesk.appMainActivity);
            cv.put(deskHelp.APP_POINT_PARENTS, xyAppInfoInDesk.appPonitParents);
            cv.put(deskHelp.APP_POINT_ONE, xyAppInfoInDesk.appPonitOne);
            cv.put(deskHelp.APP_POINT_TWO, xyAppInfoInDesk.appPonitTwo);
            sd.insert(deskHelp.TABLE_Name, null, cv);
        }
    }

    //删除屏幕上app
    public void deleApp(String appPackage) {
        addDeleApp(appPackage);
        SQLiteDatabase sd = deskHelp.getWritableDatabase();
        sd.execSQL("delete from " + deskHelp.TABLE_Name + " where " + deskHelp.APP_PACKAGE_NAME + "=?", new String[]{appPackage});
    }

    //判断应用是否存在
    public boolean isExits(String packageName) {
        SQLiteDatabase sd = deskHelp.getReadableDatabase();
        Cursor cs = null;
        boolean isExits = false;
        try {
            cs = sd.rawQuery("select * from " + deskHelp.TABLE_Name + " where " + deskHelp.APP_PACKAGE_NAME + "=?", new String[]{packageName});
            isExits = cs != null && cs.moveToFirst();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cs != null) {
                cs.close();
            }
        }
        return isExits;
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

    //记录已删除的APP
    public void addDeleApp(String appPackageName) {
        if (!isExistApp(appPackageName)) {
            SQLiteDatabase sd = deskHelp.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(deskHelp.DELE_APP_PACKAGE_NAME, appPackageName);
            sd.insert(deskHelp.TABLE_DELE_REC_NAME, null, cv);
        }
    }

    //常用删除记录里是否有原纪录
    public boolean isExistApp(String appPackageName) {
        SQLiteDatabase sd = null;
        Cursor cs = null;
        boolean isExits = false;
        try {
            sd = deskHelp.getReadableDatabase();
            cs = sd.rawQuery("select * from " + deskHelp.TABLE_DELE_REC_NAME + " where " + deskHelp.DELE_APP_PACKAGE_NAME + "=?", new String[]{appPackageName});
            isExits = cs != null && cs.moveToFirst();
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
        return isExits;
    }

    /**
     * APP语音
     */
    //添加语音设置的APP名称
    public void addSetAppName(XYXFNameSetModel xyxfNameSetModel) {
        if (isExistAppName(xyxfNameSetModel.set_app_name)) {
            Utils.getInstance().toast("名称已被占用");
            return;
        }
        SQLiteDatabase sd = null;
        ContentValues contentValues;
        try {
            sd = deskHelp.getWritableDatabase();
            contentValues = new ContentValues();
            contentValues.put(deskHelp.APP_SET_NAME, xyxfNameSetModel.set_app_name);
            contentValues.put(deskHelp.APP_SET_PACKAGE_NAME, xyxfNameSetModel.set_app_package_name);
            sd.insert(deskHelp.TABLE_APP_NAME_SET_NAME, null, contentValues);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sd != null) {
                sd.close();
            }
        }
    }

    /*//修改语音设置的APP名称
    public void updateAppName(XYXFNameSetModel xyxfNameSetModel) {
        SQLiteDatabase sd = null;
        try {
            sd = deskHelp.getWritableDatabase();
            sd.rawQuery("update " + deskHelp.TABLE_APP_NAME_SET_NAME + " set " + deskHelp.APP_SET_NAME + "=?" + " where " + deskHelp.APP_SET_PACKAGE_NAME + "=?",
                    new String[]{xyxfNameSetModel.set_app_name, xyxfNameSetModel.set_app_package_name});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sd != null) {
                sd.close();
            }
        }
    }*/

    //根据名称获取包名
    public String getAppPackageName(String appName) {
        SQLiteDatabase sd = null;
        Cursor cs = null;
        String packageName = XYContant.F;
        try {
            sd = deskHelp.getReadableDatabase();
            cs = sd.rawQuery("select * from " + deskHelp.TABLE_APP_NAME_SET_NAME + " where " + deskHelp.APP_SET_NAME + "=?", new String[]{appName});
            if (cs != null && cs.moveToFirst()) {
                packageName = getString(cs, deskHelp.APP_SET_PACKAGE_NAME);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sd != null) {
                sd.close();
            }
            if (cs != null) {
                cs.close();
            }
        }
        return packageName;
    }

    //查询是否有这个app名字
    private boolean isExistAppName(String appName) {
        SQLiteDatabase sd = null;
        Cursor cs = null;
        boolean isExits = false;
        try {
            sd = deskHelp.getReadableDatabase();
            cs = sd.rawQuery("select * from " + deskHelp.TABLE_APP_NAME_SET_NAME + " where " + deskHelp.APP_SET_NAME + "=?", new String[]{appName});
            isExits = cs != null && cs.moveToFirst();
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
        return isExits;
    }

    /**
     * APP联系人
     */
    //添加语音设置的联系人的名字
    public void addSetContactName(XYXFNameSetModel xyxfNameSetModel) {
        if (isExistContactName(xyxfNameSetModel.set_contact_name)) {
            Utils.getInstance().toast("名称已被占用");
            return;
        }
        SQLiteDatabase sd = null;
        ContentValues contentValues;
        try {
            sd = deskHelp.getWritableDatabase();
            contentValues = new ContentValues();
            contentValues.put(deskHelp.CONTACT_NAME, xyxfNameSetModel.set_contact_name);
            contentValues.put(deskHelp.CONTACT_NUMBER, xyxfNameSetModel.set_contact_number);
            sd.insert(deskHelp.TABLE_CONTACT_NAME_SET_NAME, null, contentValues);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sd != null) {
                sd.close();
            }
        }
    }

    /*//修改语音设置的联系人的名字
    public void updateContactName(XYXFNameSetModel xyxfNameSetModel) {
        SQLiteDatabase sd = null;
        try {
            sd = deskHelp.getWritableDatabase();
            sd.rawQuery("update " + deskHelp.TABLE_CONTACT_NAME_SET_NAME + " set " + deskHelp.CONTACT_NAME + "=?" + " where " + deskHelp.CONTACT_NUMBER + "=?",
                    new String[]{xyxfNameSetModel.set_contact_name, xyxfNameSetModel.set_contact_number});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sd != null) {
                sd.close();
            }
        }
    }*/

    //查询是否有这个名字
    private boolean isExistContactName(String contactName) {
        SQLiteDatabase sd = null;
        Cursor cs = null;
        boolean isExits = false;
        try {
            sd = deskHelp.getReadableDatabase();
            cs = sd.rawQuery("select * from " + deskHelp.TABLE_CONTACT_NAME_SET_NAME + " where " + deskHelp.CONTACT_NAME + "=?", new String[]{contactName});
            isExits = cs != null && cs.moveToFirst();
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
        return isExits;
    }

    //根据名称获取电话号码
    public String getContactNum(String contactName) {
        SQLiteDatabase sd = null;
        Cursor cs = null;
        String packageName = XYContant.F;
        try {
            sd = deskHelp.getReadableDatabase();
            cs = sd.rawQuery("select * from " + deskHelp.TABLE_CONTACT_NAME_SET_NAME + " where " + deskHelp.CONTACT_NAME + "=?", new String[]{contactName});
            if (cs != null && cs.moveToFirst()) {
                packageName = getString(cs, deskHelp.CONTACT_NUMBER);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sd != null) {
                sd.close();
            }
            if (cs != null) {
                cs.close();
            }
        }
        return packageName;
    }

    private String getString(Cursor cs, String index) {
        return cs.getString(cs.getColumnIndex(index));
    }
}
