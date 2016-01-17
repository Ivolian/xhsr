package unicorn.com.xhsr;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.iflytek.cloud.SpeechUtility;

import unicorn.com.xhsr.greendao.DaoMaster;
import unicorn.com.xhsr.greendao.DaoSession;
import unicorn.com.xhsr.greendao.ProcessingModeDao;


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

    public static ProcessingModeDao getProcessingModeDao() {
        return daoSession.getProcessingModeDao();
    }

    private void initGreenDao() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "xhsr-db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

}
