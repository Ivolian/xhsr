package unicorn.com.xhsr;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.facebook.drawee.backends.pipeline.Fresco;

import unicorn.com.xhsr.data.greendao.DaoMaster;
import unicorn.com.xhsr.data.greendao.DaoSession;
import unicorn.com.xhsr.volley.SimpleVolley;


public class SimpleApplication extends Application {

    private static SimpleApplication instance;

    public static SimpleApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {

        super.onCreate();
        instance = this;
        initGreenDao();
        SimpleVolley.init(instance);
        Fresco.initialize(instance);
//        LeakCanary.install(this);
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
