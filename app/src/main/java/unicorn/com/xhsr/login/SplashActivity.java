package unicorn.com.xhsr.login;

import android.os.Bundle;
import android.os.Handler;

import unicorn.com.xhsr.base.BaseActivity;
import unicorn.com.xhsr.other.TinyDB;
import unicorn.com.xhsr.utils.SharedPreferencesUtils;
import unicorn.com.xhsr.weather.WeatherModuler;


public class SplashActivity extends BaseActivity {

    private WeatherModuler.SimpleInterface simpleInterface = new WeatherModuler.SimpleInterface() {
        @Override
        public void afterFetchWeatherInfo() {
            int delay = 500;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doTheRealThing();
                }
            }, delay);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WeatherModuler weatherModuler = new WeatherModuler(simpleInterface);
        weatherModuler.fetchWeatherInfo();
    }

    private void doTheRealThing() {
        TinyDB tinyDB = TinyDB.getInstance();
        boolean rememberMe = tinyDB.getBoolean(SharedPreferencesUtils.HAS_LOGIN);
        if (rememberMe) {
            String account = tinyDB.getString(SharedPreferencesUtils.ACCOUNT);
            String password = tinyDB.getString(SharedPreferencesUtils.PASSWORD);
            LoginModuler loginModuler = new LoginModuler(account, password);
            loginModuler.login(this, false);
        } else {
            startActivityAndFinish(LoginActivity.class);
        }
    }

}
