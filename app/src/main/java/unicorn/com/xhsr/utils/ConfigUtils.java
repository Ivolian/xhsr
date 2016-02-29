package unicorn.com.xhsr.utils;


import com.android.volley.NetworkResponse;

import unicorn.com.xhsr.other.TinyDB;

public class ConfigUtils {


    public final static String JSESSION_ID = "jsessionid";


    public static void saveJSessionId(NetworkResponse response) {
        TinyDB.getInstance().putString(ConfigUtils.JSESSION_ID, response.headers.get(ConfigUtils.JSESSION_ID));
    }

    public static String getJsessionId() {
        return TinyDB.getInstance().getString(ConfigUtils.JSESSION_ID);
    }

    //

//    final static String ip = "withub.net.cn";
    final static String ip = "192.168.7.65";

    final static String port = "80";

    public static String getBaseUrl() {
        return "http://" + ip + ":" + port + "/hems";
    }

}
