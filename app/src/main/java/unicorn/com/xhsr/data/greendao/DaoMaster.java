package unicorn.com.xhsr.data.greendao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import de.greenrobot.dao.AbstractDaoMaster;
import de.greenrobot.dao.identityscope.IdentityScopeType;

import unicorn.com.xhsr.data.greendao.ProcessingModeDao;
import unicorn.com.xhsr.data.greendao.ProcessingTimeLimitDao;
import unicorn.com.xhsr.data.greendao.EmergencyDegreeDao;
import unicorn.com.xhsr.data.greendao.EquipmentCategoryDao;
import unicorn.com.xhsr.data.greendao.EquipmentDao;
import unicorn.com.xhsr.data.greendao.BuildingDao;
import unicorn.com.xhsr.data.greendao.DepartmentDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * Master of DAO (schema version 9): knows all DAOs.
*/
public class DaoMaster extends AbstractDaoMaster {
    public static final int SCHEMA_VERSION = 9;

    /** Creates underlying database table using DAOs. */
    public static void createAllTables(SQLiteDatabase db, boolean ifNotExists) {
        ProcessingModeDao.createTable(db, ifNotExists);
        ProcessingTimeLimitDao.createTable(db, ifNotExists);
        EmergencyDegreeDao.createTable(db, ifNotExists);
        EquipmentCategoryDao.createTable(db, ifNotExists);
        EquipmentDao.createTable(db, ifNotExists);
        BuildingDao.createTable(db, ifNotExists);
        DepartmentDao.createTable(db, ifNotExists);
    }
    
    /** Drops underlying database table using DAOs. */
    public static void dropAllTables(SQLiteDatabase db, boolean ifExists) {
        ProcessingModeDao.dropTable(db, ifExists);
        ProcessingTimeLimitDao.dropTable(db, ifExists);
        EmergencyDegreeDao.dropTable(db, ifExists);
        EquipmentCategoryDao.dropTable(db, ifExists);
        EquipmentDao.dropTable(db, ifExists);
        BuildingDao.dropTable(db, ifExists);
        DepartmentDao.dropTable(db, ifExists);
    }
    
    public static abstract class OpenHelper extends SQLiteOpenHelper {

        public OpenHelper(Context context, String name, CursorFactory factory) {
            super(context, name, factory, SCHEMA_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.i("greenDAO", "Creating tables for schema version " + SCHEMA_VERSION);
            createAllTables(db, false);
        }
    }
    
    /** WARNING: Drops all table on Upgrade! Use only during development. */
    public static class DevOpenHelper extends OpenHelper {
        public DevOpenHelper(Context context, String name, CursorFactory factory) {
            super(context, name, factory);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.i("greenDAO", "Upgrading schema from version " + oldVersion + " to " + newVersion + " by dropping all tables");
            dropAllTables(db, true);
            onCreate(db);
        }
    }

    public DaoMaster(SQLiteDatabase db) {
        super(db, SCHEMA_VERSION);
        registerDaoClass(ProcessingModeDao.class);
        registerDaoClass(ProcessingTimeLimitDao.class);
        registerDaoClass(EmergencyDegreeDao.class);
        registerDaoClass(EquipmentCategoryDao.class);
        registerDaoClass(EquipmentDao.class);
        registerDaoClass(BuildingDao.class);
        registerDaoClass(DepartmentDao.class);
    }
    
    public DaoSession newSession() {
        return new DaoSession(db, IdentityScopeType.Session, daoConfigMap);
    }
    
    public DaoSession newSession(IdentityScopeType type) {
        return new DaoSession(db, type, daoConfigMap);
    }
    
}
