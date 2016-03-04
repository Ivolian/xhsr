package unicorn.com.xhsr;

import android.os.Bundle;
import android.os.Handler;

import unicorn.com.xhsr.base.BaseActivity;
import unicorn.com.xhsr.moduler.LoginModuler;
import unicorn.com.xhsr.other.TinyDB;
import unicorn.com.xhsr.utils.SfUtils;


public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int delay = 1000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doTheRealThing();
            }
        }, delay);
    }

    private void doTheRealThing() {
        TinyDB tinyDB = TinyDB.getInstance();
        boolean rememberMe = tinyDB.getBoolean(SfUtils.SF_REMEMBER_ME);
        if (rememberMe) {
            String account = tinyDB.getString(SfUtils.SF_ACCOUNT);
            String password = tinyDB.getString(SfUtils.SF_PASSWORD);
            LoginModuler loginModuler = new LoginModuler(account, password);
            loginModuler.login(this, false);
        } else {
            startActivityAndFinish(LoginActivity.class);
        }
    }

}
