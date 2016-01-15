package unicorn.com.xhsr;

import android.app.Application;

import com.iflytek.cloud.SpeechUtility;


public class SimpleApplication extends Application {

    private static SimpleApplication instance;

    public static SimpleApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {

        SpeechUtility.createUtility(this, "appid=" + "5697584f");

        super.onCreate();
        instance = this;
//        SimpleVolley.init(instance);
    }

}
