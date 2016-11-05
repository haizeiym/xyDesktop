package xydesk.xy.voice;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.sunflower.FlowerCollector;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Timer;
import java.util.TimerTask;

import xydesk.xy.i.VoiceI;
import xydesk.xy.utils.Utils;

/**
 * Created by haizeiym
 * on 2016/8/30
 */
public class VoiceUtils {
    Context context;
    // 语音听写对象
    private SpeechRecognizer mIat;
    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<>();
    private int ret = 0; // 函数调用返回值
    private boolean isClickDouble = false;
    private VoiceI voiceI;

    public VoiceUtils(final Context context) {
        this.context = context;
        /**
         *  初始化监听器。
         */
        InitListener mInitListener = new InitListener() {

            @Override
            public void onInit(int code) {
                if (code != ErrorCode.SUCCESS) {
                    Utils.getInstance().toast(context, "初始化失败，错误码：" + code);
                }
            }
        };
        mIat = SpeechRecognizer.createRecognizer(context, mInitListener);
        FlowerCollector.onEvent(context, "iat_recognize");
        // 设置参数
        setParam();
    }

    public void startVoice(VoiceI voiceI) {
        if (!Utils.getInstance().isNetAvailable(context)) {
            Utils.getInstance().toast(context, "网络不可用");
            return;
        }
        this.voiceI = voiceI;
        if (!isClickDouble) {
            try {
                isClickDouble = true;
                Utils.getInstance().toast(context, "请说话");
                Timer timer = new Timer();
                TimerTask t = new TimerTask() {
                    @Override
                    public void run() {
                        mIatResults.clear();
                        ret = mIat.startListening(mRecognizerListener);
                        if (ret != ErrorCode.SUCCESS) {
                            Utils.getInstance().toast(context, "听写失败,错误码：" + ret);
                        }
                    }
                };
                timer.schedule(t, 679);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setParam() {
        // 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);
        // 设置听写引擎
        String mEngineType = SpeechConstant.TYPE_CLOUD;
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");
        // 设置语言
        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        // 设置语言区域
        mIat.setParameter(SpeechConstant.ACCENT, "mandarin");
        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS, "999");
        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, "999");
        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, "0");
        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/iat.wav");
    }

    /**
     * 听写监听器。
     */
    private RecognizerListener mRecognizerListener = new RecognizerListener() {

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
            //"开始说话";
        }

        @Override
        public void onError(SpeechError error) {
            // Tips：
            // 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
            // 如果使用本地功能（语记）需要提示用户开启语记的录音权限。
            if (error.getErrorCode() == 10118) {
                Utils.getInstance().toast(context, "请查看权限是否打开");
            }
        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
            Utils.getInstance().toast(context, "已结束");
            isClickDouble = false;
        }

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            if (!isLast) {
                printResult(results);
            }
        }

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
            // Utils.getInstance().toast(instance, "当前正在说话，音量大小：" + volume);
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            // if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            // String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            // Log.d(TAG, "session id =" + sid);
            // }
        }
    };

    private void printResult(RecognizerResult results) {
        String text = JsonParser.parseIatResult(results.getResultString());
        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mIatResults.put(sn, text);
        StringBuilder resultBuffer = new StringBuilder();
        for (String key : mIatResults.keySet()) {
            resultBuffer.append(mIatResults.get(key));
        }
        String userString = resultBuffer.toString();
        //语音结果返回
        voiceI.voiceResult(userString);
    }
}
