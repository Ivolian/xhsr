package unicorn.com.xhsr.data.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import unicorn.com.xhsr.data.greendao.ProcessingTimeLimit;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "PROCESSING_TIME_LIMIT".
*/
public class ProcessingTimeLimitDao extends AbstractDao<ProcessingTimeLimit, String> {

    public static final String TABLENAME = "PROCESSING_TIME_LIMIT";

    /**
     * Properties of entity ProcessingTimeLimit.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property ObjectId = new Property(0, String.class, "objectId", true, "OBJECT_ID");
        public final static Property Name = new Property(1, String.class, "name", false, "NAME");
        public final static Property OrderNo = new Property(2, int.class, "orderNo", false, "ORDER_NO");
    };


    public ProcessingTimeLimitDao(DaoConfig config) {
        super(config);
    }
    
    public ProcessingTimeLimitDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"PROCESSING_TIME_LIMIT\" (" + //
                "\"OBJECT_ID\" TEXT PRIMARY KEY NOT NULL ," + // 0: objectId
                "\"NAME\" TEXT NOT NULL ," + // 1: name
                "\"ORDER_NO\" INTEGER NOT NULL );"); // 2: orderNo
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"PROCESSING_TIME_LIMIT\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, ProcessingTimeLimit entity) {
        stmt.clearBindings();
        stmt.bindString(1, entity.getObjectId());
        stmt.bindString(2, entity.getName());
        stmt.bindLong(3, entity.getOrderNo());
    }

    /** @inheritdoc */
    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.getString(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public ProcessingTimeLimit readEntity(Cursor cursor, int offset) {
        ProcessingTimeLimit entity = new ProcessingTimeLimit( //
            cursor.getString(offset + 0), // objectId
            cursor.getString(offset + 1), // name
            cursor.getInt(offset + 2) // orderNo
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, ProcessingTimeLimit entity, int offset) {
        entity.setObjectId(cursor.getString(offset + 0));
        entity.setName(cursor.getString(offset + 1));
        entity.setOrderNo(cursor.getInt(offset + 2));
     }
    
    /** @inheritdoc */
    @Override
    protected String updateKeyAfterInsert(ProcessingTimeLimit entity, long rowId) {
        return entity.getObjectId();
    }
    
    /** @inheritdoc */
    @Override
    public String getKey(ProcessingTimeLimit entity) {
        if(entity != null) {
            return entity.getObjectId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
