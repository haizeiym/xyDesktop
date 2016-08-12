package xydesk.xy.base;

import android.os.Handler;
import android.os.Message;

/**
 * Created by haizeiym
 * on 2016/7/27
 */
public abstract class XYBaseHandler extends Handler {
    @Override
    public void handleMessage(Message msg) {
        handlerMsg(msg);
    }

    public abstract void handlerMsg(Message msg);
}
