package xydesk.xy.servce;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import xydesk.xy.appAll.ui.AllAppShowUI;
import xydesk.xy.contant.XYContant;
import xydesk.xy.utils.AppUtils;

/**
 * Created by Administrator on 2016/10/27.
 */

public class MyInstalledReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if (AllAppShowUI.staticInstance != null) {
            if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {  // install
                AllAppShowUI.staticInstance.handler.sendEmptyMessage(XYContant.XYContants.ADD_APP);
            } else if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")) { // uninstall
                String packageName = intent.getDataString().substring(8, intent.getDataString().length());
                AppUtils.getInstance().deleAtFragment(context, packageName);
                AllAppShowUI.staticInstance.handler.sendEmptyMessage(XYContant.XYContants.DELETER_APP);
            }
        }
    }
}
