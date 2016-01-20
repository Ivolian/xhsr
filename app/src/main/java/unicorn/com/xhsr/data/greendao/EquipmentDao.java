package unicorn.com.xhsr.data.greendao;

import java.util.List;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;
import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "EQUIPMENT".
*/
public class EquipmentDao extends AbstractDao<Equipment, Void> {

    public static final String TABLENAME = "EQUIPMENT";

    /**
     * Properties of entity Equipment.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property ObjectId = new Property(0, String.class, "objectId", false, "OBJECT_ID");
        public final static Property Name = new Property(1, String.class, "name", false, "NAME");
        public final static Property OrderNo = new Property(2, int.class, "orderNo", false, "ORDER_NO");
        public final static Property CategoryId = new Property(3, String.class, "categoryId", false, "CATEGORY_ID");
    };

    private Query<Equipment> equipmentCategory_EquipmentListQuery;

    public EquipmentDao(DaoConfig config) {
        super(config);
    }
    
    public EquipmentDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"EQUIPMENT\" (" + //
                "\"OBJECT_ID\" TEXT NOT NULL ," + // 0: objectId
                "\"NAME\" TEXT NOT NULL ," + // 1: name
                "\"ORDER_NO\" INTEGER NOT NULL ," + // 2: orderNo
                "\"CATEGORY_ID\" TEXT NOT NULL );"); // 3: categoryId
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"EQUIPMENT\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Equipment entity) {
        stmt.clearBindings();
        stmt.bindString(1, entity.getObjectId());
        stmt.bindString(2, entity.getName());
        stmt.bindLong(3, entity.getOrderNo());
        stmt.bindString(4, entity.getCategoryId());
    }

    /** @inheritdoc */
    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    /** @inheritdoc */
    @Override
    public Equipment readEntity(Cursor cursor, int offset) {
        Equipment entity = new Equipment( //
            cursor.getString(offset + 0), // objectId
            cursor.getString(offset + 1), // name
            cursor.getInt(offset + 2), // orderNo
            cursor.getString(offset + 3) // categoryId
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Equipment entity, int offset) {
        entity.setObjectId(cursor.getString(offset + 0));
        entity.setName(cursor.getString(offset + 1));
        entity.setOrderNo(cursor.getInt(offset + 2));
        entity.setCategoryId(cursor.getString(offset + 3));
     }
    
    /** @inheritdoc */
    @Override
    protected Void updateKeyAfterInsert(Equipment entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    /** @inheritdoc */
    @Override
    public Void getKey(Equipment entity) {
        return null;
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
    /** Internal query to resolve the "equipmentList" to-many relationship of EquipmentCategory. */
    public List<Equipment> _queryEquipmentCategory_EquipmentList(String categoryId) {
        synchronized (this) {
            if (equipmentCategory_EquipmentListQuery == null) {
                QueryBuilder<Equipment> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.CategoryId.eq(null));
                queryBuilder.orderRaw("T.'ORDER_NO' ASC");
                equipmentCategory_EquipmentListQuery = queryBuilder.build();
            }
        }
        Query<Equipment> query = equipmentCategory_EquipmentListQuery.forCurrentThread();
        query.setParameter(0, categoryId);
        return query.list();
    }

}