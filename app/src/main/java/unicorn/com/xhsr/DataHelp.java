package unicorn.com.xhsr;

import java.util.List;

import unicorn.com.xhsr.greendao.ProcessingMode;
import unicorn.com.xhsr.greendao.ProcessingModeDao;


public class DataHelp {

    public static List<ProcessingMode> getProcessModeList() {
        return SimpleApplication.getProcessingModeDao()
                .queryBuilder()
                .orderAsc(ProcessingModeDao.Properties.OrderNo)
                .list();
    }

}
