package unicorn.com.xhsr;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.iflytek.cloud.SpeechUtility;

import unicorn.com.xhsr.greendao.DaoMaster;
import unicorn.com.xhsr.greendao.DaoSession;


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
        initGreenDao();
        SimpleVolley.init(instance);
    }


    private static DaoSession daoSession;

    public static DaoSession getDaoSession() {
        return daoSession;
    }


    private void initGreenDao() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "xhsr-db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

}
