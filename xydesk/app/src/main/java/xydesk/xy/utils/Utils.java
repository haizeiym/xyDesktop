package xydesk.xy.utils;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import xydesk.xy.model.XYAllAppModel;

/**
 * Created by haizeiym
 * on 2016/8/4
 */
public class Utils {
    public static Utils getInstance() {
        return UtilsInstance.instance;
    }

    private Utils() {
    }

    private static class UtilsInstance {
        private static final Utils instance = new Utils();
    }

    //DrawableToBitmap
    public Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(
                drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    //Toast工具
    public void toast(Context context, String content) {
        final Toast toast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
        toast.show();
        Timer timer = new Timer();
        TimerTask t = new TimerTask() {
            @Override
            public void run() {
                toast.cancel();
            }
        };
        timer.schedule(t, 41);
    }

    //名称字母排序
    public List<XYAllAppModel> filledChar(List<XYAllAppModel> xyAllAppModelList, CharacterParser characterParser) {
        List<XYAllAppModel> listC = new ArrayList<>();
        for (int i = 0; i < xyAllAppModelList.size(); i++) {
            XYAllAppModel xyAllAppModel = xyAllAppModelList.get(i);
            String appName = xyAllAppModel.appName;
            String appPackage = xyAllAppModel.appPackageName;
            String appMainActivity = xyAllAppModel.activityMainName;
            String version = xyAllAppModel.appVersion;
            Drawable appIcon = xyAllAppModel.appIcon;
            //汉字转换成拼音
            String pinyin = characterParser.getSelling(appName);
            String sortString = pinyin.substring(0, 1).toUpperCase();
            String sortLetter;
            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                sortLetter = sortString.toUpperCase();
            } else {
                sortLetter = "#";
            }
            XYAllAppModel xyAllAppModel1 = new XYAllAppModel(appName, appPackage, appIcon, sortLetter, appMainActivity, version);
            listC.add(xyAllAppModel1);
        }
        return listC;
    }

    //判断网络是否可用
    public boolean isNetAvailable(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        return (info != null && info.isAvailable());
    }

    //音频播放
    public void playOgg(Context context) {
//
        try {
            AssetFileDescriptor fileDescriptor = context.getAssets().openFd("start_end.ogg");
            final MediaPlayer mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(
                        fileDescriptor.getFileDescriptor(),
                        fileDescriptor.getStartOffset(),
                        fileDescriptor.getLength());
                mediaPlayer.setOnPreparedListener(preparedListener);
                mediaPlayer.prepareAsync();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private MediaPlayer.OnPreparedListener preparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            mp.start();
        }
    };
}
