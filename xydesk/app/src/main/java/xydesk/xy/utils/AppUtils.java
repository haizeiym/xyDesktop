package xydesk.xy.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xydesk.xy.MainActivity;
import xydesk.xy.contant.XYContant;
import xydesk.xy.db.DeskDB;
import xydesk.xy.model.XYAllAppModel;
import xydesk.xy.model.XYAppInfoInDesk;

/**
 * Created by haizeiym
 * on 2016/7/28
 */
public class AppUtils {
    public static AppUtils getInstance() {
        return UtilsInstance.instance;
    }

    public static Map<String, String> allAppName = new HashMap<>();
    private AppUtils() {

    }

    private static class UtilsInstance {
        private static final AppUtils instance = new AppUtils();
    }

    public final String APP_PACKAGE = "xydesk.xy.xydesk";
    private final String[] uApp = {"com.eg.android.AlipayGphone", "com.sina.weibo", "com.tencent.mobileqq", "com.tencent.mm"};
    public String delePackageName = "";

    //获取所有APP列表
    public List<XYAllAppModel> getAllAppList(Context context) {
        allAppName.clear();
        List<XYAllAppModel> xyModels = new ArrayList<>();
        try {
            PackageManager packageManager = context.getPackageManager();
            List<ResolveInfo> apps = getAllApp(packageManager);
            for (int i = 0; i < apps.size(); i++) {
                XYAllAppModel xyModel = new XYAllAppModel();
                ResolveInfo resolveInfo = apps.get(i);
                if (!resolveInfo.activityInfo.packageName.equals(APP_PACKAGE)) {
                    String p = resolveInfo.activityInfo.packageName;
                    String n = resolveInfo.loadLabel(packageManager).toString();
                    PackageInfo packageInfo = packageManager.getPackageInfo(p, 0);
                    xyModel.appVersion = packageInfo.versionName + ": " + packageInfo.versionCode;
                    xyModel.activityMainName = resolveInfo.activityInfo.name;
                    xyModel.appPackageName = p;
                    xyModel.appName = n;
                    allAppName.put(n, p);
                    xyModel.appIcon = resolveInfo.loadIcon(packageManager);
                    xyModels.add(xyModel);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return xyModels;
    }

    //App删除
    public void delApp(String packageName) {
        if (packageName.isEmpty()) {
            return;
        }
        delePackageName = packageName;
        Uri uri = Uri.parse("package:" + packageName);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DELETE);
        intent.setData(uri);
        MainActivity.instance.startActivityForResult(intent, XYContant.DELETER_APP);
    }

    //根据包名打开APP
    public void openApp(Context context, String appPackageName) {
        /*//应用的主类名
        String cls = xyAllAppModel.activityMainName;
        ComponentName componentName = new ComponentName(pkg, cls);
        Intent intent = new Intent();
        intent.setComponent(componentName);
        context.startActivity(intent);*/
        PackageManager pm = context.getPackageManager();
        Intent i = pm.getLaunchIntentForPackage(appPackageName);
        //应用是否存在
        if (i != null) {
            context.startActivity(i);
        }
    }

    //获取所有应用
    private List<ResolveInfo> getAllApp(PackageManager packageManager) {
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        return packageManager.queryIntentActivities(intent, 0);
    }

    /*//根据包名获取图标(影响效率，加载时会比较慢)
    public Bitmap getIconFromPackName(Context context, String packageName) {
        Bitmap icon = null;
        for (XYAllAppModel xyAllAppModel : getAllAppList(context)) {
            if (packageName.equals(xyAllAppModel.appPackageName)) {
                icon = Utils.getInstance().drawableToBitmap(xyAllAppModel.appIcon);
                break;
            }
        }
        return icon;
    }*/

    //常用APP
    public void getAppU(Context context) {
        DeskDB deskDB = new DeskDB(context);
        for (XYAllAppModel xyAllAppModel : getAllAppList(context)) {
            for (String appPackageName : uApp) {
                if (!deskDB.isExits(appPackageName) && xyAllAppModel.appPackageName.equals(appPackageName)) {
                    if (!deskDB.isExistApp(appPackageName)) {
                        XYAppInfoInDesk xyAppInfoInDesk = new XYAppInfoInDesk();
                        xyAppInfoInDesk.appName = xyAllAppModel.appName;
                        xyAppInfoInDesk.appPonitParents = XYContant.ONE_FRAGMENT;
                        xyAppInfoInDesk.appPackageName = xyAllAppModel.appPackageName;
                        deskDB.addAppInfo(xyAppInfoInDesk);
                    } else {
                        deskDB.deleApp(appPackageName);
                    }
                }
            }
        }
    }

    //删除屏幕上APP
    public void deleAtFragment(Context context, String appPakcageName) {
        DeskDB deskDB = new DeskDB(context);
        deskDB.deleApp(appPakcageName);
    }

    //根据pointParents获取所有app信息
    public List<XYAppInfoInDesk> getAllApp(Context context, String parentPoint) {
        DeskDB deskDB = new DeskDB(context);
        return deskDB.getAllApp(parentPoint);
    }

    //强制停止应用程序
    public void forceStopPackage(String pkgName, Context context) throws Exception {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        Method method = Class.forName("android.app.ActivityManager").getMethod("forceStopPackage", String.class);
        method.invoke(am, pkgName);
    }

    public void appIsSave(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("bottomName", 1);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isSave", true);
        editor.commit();
    }
}
