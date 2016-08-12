package xydesk.xy.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import xydesk.xy.contant.XYContant;
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

    private AppUtils() {

    }

    private static class UtilsInstance {
        private static final AppUtils instance = new AppUtils();
    }

    public final String APP_PACKAGE = "xydesk.xy.xydesk";

    //获取所有APP列表
    public List<XYAllAppModel> getAllAppList(Context context) {
        List<XYAllAppModel> xyModels = new ArrayList<>();
        try {
            PackageManager packageManager = context.getPackageManager();
            List<ResolveInfo> apps = getAllApp(packageManager);
            for (int i = 0; i < apps.size(); i++) {
                XYAllAppModel xyModel = new XYAllAppModel();
                ResolveInfo resolveInfo = apps.get(i);
                if (!resolveInfo.activityInfo.packageName.equals(APP_PACKAGE)) {
                    xyModel.appPackageName = resolveInfo.activityInfo.packageName;
//                xyModel.activityMainName = resolveInfo.activityInfo.name;
                    xyModel.appName = resolveInfo.loadLabel(packageManager).toString();
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
    public void delApp(Activity activity, String packageName) {
        if (packageName.isEmpty()) {
            return;
        }
        Uri uri = Uri.parse("package:" + packageName);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DELETE);
        intent.setData(uri);
        activity.startActivityForResult(intent, XYContant.DELETER_APP);
    }

    //根据包名打开APP
    public void openApp(Context context, String appPackageName) {
        //应用包名
        if (appPackageName.isEmpty()) {
            Utils.getInstance().toast(context, "应用已不存在");
            return;
        }
//        //应用的主类名
//        String cls = xyAllAppModel.activityMainName;
//        ComponentName componentName = new ComponentName(pkg, cls);
//        Intent intent = new Intent();
//        intent.setComponent(componentName);
//        context.startActivity(intent);
        PackageManager pm = context.getPackageManager();
        Intent i = pm.getLaunchIntentForPackage(appPackageName);
        context.startActivity(i);
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

    String[] uApp = {"com.ca.tongxunlu", "com.eg.android.AlipayGphone", "com.sina.weibo", "com.tencent.mobileqq", "com.tencent.mm"};

    //常用APP集锦
    public Set<XYAppInfoInDesk> getAppU() {
        Set<XYAppInfoInDesk> deskSet = new HashSet<>();

        return deskSet;
    }

}
