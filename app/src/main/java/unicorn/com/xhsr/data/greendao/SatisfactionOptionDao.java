package unicorn.com.xhsr.data.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import unicorn.com.xhsr.data.greendao.SatisfactionOption;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "SATISFACTION_OPTION".
*/
public class SatisfactionOptionDao extends AbstractDao<SatisfactionOption, String> {

    public static final String TABLENAME = "SATISFACTION_OPTION";

    /**
     * Properties of entity SatisfactionOption.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property ObjectId = new Property(0, String.class, "objectId", true, "OBJECT_ID");
        public final static Property Title = new Property(1, String.class, "title", false, "TITLE");
        public final static Property Content = new Property(2, String.class, "content", false, "CONTENT");
        public final static Property Numerator = new Property(3, int.class, "numerator", false, "NUMERATOR");
        public final static Property Denominator = new Property(4, int.class, "denominator", false, "DENOMINATOR");
        public final static Property OrderNo = new Property(5, int.class, "orderNo", false, "ORDER_NO");
        public final static Property Score = new Property(6, int.class, "score", false, "SCORE");
    };


    public SatisfactionOptionDao(DaoConfig config) {
        super(config);
    }
    
    public SatisfactionOptionDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"SATISFACTION_OPTION\" (" + //
                "\"OBJECT_ID\" TEXT PRIMARY KEY NOT NULL ," + // 0: objectId
                "\"TITLE\" TEXT NOT NULL ," + // 1: title
                "\"CONTENT\" TEXT NOT NULL ," + // 2: content
                "\"NUMERATOR\" INTEGER NOT NULL ," + // 3: numerator
                "\"DENOMINATOR\" INTEGER NOT NULL ," + // 4: denominator
                "\"ORDER_NO\" INTEGER NOT NULL ," + // 5: orderNo
                "\"SCORE\" INTEGER NOT NULL );"); // 6: score
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"SATISFACTION_OPTION\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, SatisfactionOption entity) {
        stmt.clearBindings();
        stmt.bindString(1, entity.getObjectId());
        stmt.bindString(2, entity.getTitle());
        stmt.bindString(3, entity.getContent());
        stmt.bindLong(4, entity.getNumerator());
        stmt.bindLong(5, entity.getDenominator());
        stmt.bindLong(6, entity.getOrderNo());
        stmt.bindLong(7, entity.getScore());
    }

    /** @inheritdoc */
    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.getString(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public SatisfactionOption readEntity(Cursor cursor, int offset) {
        SatisfactionOption entity = new SatisfactionOption( //
            cursor.getString(offset + 0), // objectId
            cursor.getString(offset + 1), // title
            cursor.getString(offset + 2), // content
            cursor.getInt(offset + 3), // numerator
            cursor.getInt(offset + 4), // denominator
            cursor.getInt(offset + 5), // orderNo
            cursor.getInt(offset + 6) // score
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, SatisfactionOption entity, int offset) {
        entity.setObjectId(cursor.getString(offset + 0));
        entity.setTitle(cursor.getString(offset + 1));
        entity.setContent(cursor.getString(offset + 2));
        entity.setNumerator(cursor.getInt(offset + 3));
        entity.setDenominator(cursor.getInt(offset + 4));
        entity.setOrderNo(cursor.getInt(offset + 5));
        entity.setScore(cursor.getInt(offset + 6));
     }
    
    /** @inheritdoc */
    @Override
    protected String updateKeyAfterInsert(SatisfactionOption entity, long rowId) {
        return entity.getObjectId();
    }
    
    /** @inheritdoc */
    @Override
    public String getKey(SatisfactionOption entity) {
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
