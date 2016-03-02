package unicorn.com.xhsr.utils;

import android.content.Context;

import unicorn.com.xhsr.SimpleApplication;


public class AppUtils {

    public static String getVersionName() {
        Context context = SimpleApplication.getInstance();
        String packageName = context.getPackageName();
        String versionName = "";
        try {
            versionName = context.getPackageManager().getPackageInfo(packageName, 0).versionName;
        } catch (Exception e) {
            //
        }
        return versionName;
    }

}
