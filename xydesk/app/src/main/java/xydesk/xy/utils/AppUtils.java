package xydesk.xy.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;

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
    public static List<XYAppInfoInDesk> one_xyAppInfoInDesks = new ArrayList<>();
    public static List<XYAppInfoInDesk> two_xyAppInfoInDesks = new ArrayList<>();
    public static List<XYAppInfoInDesk> three_xyAppInfoInDesks = new ArrayList<>();
    public static List<XYAppInfoInDesk> four_xyAppInfoInDesks = new ArrayList<>();

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
                    /*xyModel.activityMainName = resolveInfo.activityInfo.name;*/
                    String p = resolveInfo.activityInfo.packageName;
                    String n = resolveInfo.loadLabel(packageManager).toString();
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

    //根据包名获取图标
    public Bitmap getIconFromPackName(Context context, String packageName) {
        Bitmap icon = null;
        for (XYAllAppModel xyAllAppModel : getAllAppList(context)) {
            if (packageName.equals(xyAllAppModel.appPackageName)) {
                icon = Utils.getInstance().drawableToBitmap(xyAllAppModel.appIcon);
                break;
            }
        }
        return icon;
    }

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

    //屏幕APP
    public void PingApp(Activity activity) {
        one_xyAppInfoInDesks.clear();
        two_xyAppInfoInDesks.clear();
        three_xyAppInfoInDesks.clear();
        four_xyAppInfoInDesks.clear();
        one_xyAppInfoInDesks = AppUtils.getInstance().getAllApp(activity, XYContant.ONE_FRAGMENT);
        two_xyAppInfoInDesks = AppUtils.getInstance().getAllApp(activity, XYContant.TWO_FRAGMENT);
        three_xyAppInfoInDesks = AppUtils.getInstance().getAllApp(activity, XYContant.THREE_FRAGMENT);
        four_xyAppInfoInDesks = AppUtils.getInstance().getAllApp(activity, XYContant.FOUR_FRAGMENT);
    }
}
