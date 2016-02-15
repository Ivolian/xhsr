package unicorn.com.xhsr.utils;


public class ConfigUtils {

    static String sessionId;

    public static String getSessionId() {
        if (sessionId == null) {
            throw new RuntimeException("SESSION_ID NOT INIT!");
        }
        return sessionId;
    }

    public static void setSessionId(String sessionId) {
        ConfigUtils.sessionId = sessionId;
    }

    //

    final static String ip = "withub.net.cn";
//    final static String ip = "192.168.7.67";

    final static String port = "80";

    public static String getBaseUrl() {
        return "http://" + ip + ":" + port + "/hems";
    }

}
