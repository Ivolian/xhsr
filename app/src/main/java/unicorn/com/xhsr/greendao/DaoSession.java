package unicorn.com.xhsr.greendao;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig processingModeDaoConfig;

    private final ProcessingModeDao processingModeDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        processingModeDaoConfig = daoConfigMap.get(ProcessingModeDao.class).clone();
        processingModeDaoConfig.initIdentityScope(type);

        processingModeDao = new ProcessingModeDao(processingModeDaoConfig, this);

        registerDao(ProcessingMode.class, processingModeDao);
    }
    
    public void clear() {
        processingModeDaoConfig.getIdentityScope().clear();
    }

    public ProcessingModeDao getProcessingModeDao() {
        return processingModeDao;
    }

}