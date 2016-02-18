package unicorn.com.xhsr.data;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import unicorn.com.xhsr.SimpleApplication;
import unicorn.com.xhsr.data.greendao.Building;
import unicorn.com.xhsr.data.greendao.BuildingDao;
import unicorn.com.xhsr.data.greendao.Department;
import unicorn.com.xhsr.data.greendao.DepartmentDao;
import unicorn.com.xhsr.data.greendao.EmergencyDegree;
import unicorn.com.xhsr.data.greendao.EmergencyDegreeDao;
import unicorn.com.xhsr.data.greendao.Equipment;
import unicorn.com.xhsr.data.greendao.EquipmentCategory;
import unicorn.com.xhsr.data.greendao.EquipmentCategoryDao;
import unicorn.com.xhsr.data.greendao.EquipmentDao;
import unicorn.com.xhsr.data.greendao.ProcessingMode;
import unicorn.com.xhsr.data.greendao.ProcessingModeDao;
import unicorn.com.xhsr.data.greendao.ProcessingTimeLimit;
import unicorn.com.xhsr.data.greendao.ProcessingTimeLimitDao;
import unicorn.com.xhsr.groupselect.GroupSelectActivity;
import unicorn.com.xhsr.select.SelectAdapter;
import unicorn.com.xhsr.select.SelectObject;


public class DataHelp {

    public static GroupSelectActivity.DataProvider getBuildingDataProvider() {

        return new GroupSelectActivity.DataProvider() {

            private List<SelectObject> mainDataList;

            @Override
            public String getMainId(String subId) {
                Building building = SimpleApplication.getDaoSession().getBuildingDao().queryBuilder()
                        .where(BuildingDao.Properties.ObjectId.eq(subId))
                        .unique();
                return building.getParentId();
            }

            @Override
            public List<SelectObject> getMainDataList() {
                if (mainDataList != null) {
                    return mainDataList;
                }
                final List<Building> buildingList = SimpleApplication.getDaoSession().getBuildingDao().queryBuilder()
                        .where(BuildingDao.Properties.ParentId.eq("root"))
                        .orderAsc(BuildingDao.Properties.OrderNo)
                        .list();
                List<SelectObject> mainDataList = new ArrayList<>();
                for (Building building : buildingList) {
                    SelectObject selectObject = new SelectObject();
                    selectObject.objectId = building.getObjectId();
                    selectObject.value = building.getName();
                    mainDataList.add(selectObject);
                }
                return (this.mainDataList = mainDataList);
            }

            @Override
            public List<SelectObject> getSubDataList(String mainId) {
                List<Building> buildingList = SimpleApplication.getDaoSession().getBuildingDao().queryBuilder()
                        .where(BuildingDao.Properties.ParentId.eq(mainId))
                        .orderAsc(BuildingDao.Properties.OrderNo)
                        .list();
                final List<SelectObject> subDataList = new ArrayList<>();
                for (Building building : buildingList) {
                    SelectObject selectObject = new SelectObject();
                    selectObject.objectId = building.getObjectId();
                    selectObject.value = building.getName();
                    subDataList.add(selectObject);
                }
                return subDataList;
            }

            @Override
            public List<SelectObject> getSearchResultDataList(String query) {
                List<SelectObject> searchResultDataList = new ArrayList<>();
                List<Building> children = SimpleApplication.getDaoSession().getBuildingDao().queryBuilder()
                        .where(BuildingDao.Properties.Level.eq(1))
                        .where(BuildingDao.Properties.FullName.like("%" + query + "%"))
                        .orderAsc(BuildingDao.Properties.OrderNo)
                        .list();
                for (Building child : children) {
                    SelectObject selectObject = new SelectObject();
                    selectObject.objectId = child.getObjectId();
                    selectObject.value = child.getFullName();
                    searchResultDataList.add(selectObject);
                }
                return searchResultDataList;
            }
        };

    }

    public static GroupSelectActivity.DataProvider getEquipmentDataProvider() {

        return new GroupSelectActivity.DataProvider() {

            List<SelectObject> mainDataList;

            @Override
            public List<SelectObject> getMainDataList() {
                if (mainDataList != null) {
                    return mainDataList;
                }

                // 查询设备目录所有值
                final List<EquipmentCategory> equipmentCategoryList = SimpleApplication.getDaoSession().getEquipmentCategoryDao().queryBuilder()
                        .orderAsc(EquipmentCategoryDao.Properties.OrderNo)
                        .list();
                List<SelectObject> mainDataList = new ArrayList<>();
                for (EquipmentCategory equipmentCategory : equipmentCategoryList) {
                    SelectObject selectObject = new SelectObject();
                    selectObject.objectId = equipmentCategory.getObjectId();
                    selectObject.value = equipmentCategory.getName();
                    mainDataList.add(selectObject);
                }
                return (this.mainDataList = mainDataList);
            }

            @Override
            public String getMainId(String subId) {
                Equipment equipment = SimpleApplication.getDaoSession().getEquipmentDao().queryBuilder()
                        .where(EquipmentDao.Properties.ObjectId.eq(subId))
                        .unique();
                return equipment.getCategoryId();
            }

            @Override
            public List<SelectObject> getSubDataList(String mainId) {
                List<Equipment> equipmentList = SimpleApplication.getDaoSession().getEquipmentDao().queryBuilder()
                        .where(EquipmentDao.Properties.CategoryId.eq(mainId))
                        .orderAsc(EquipmentDao.Properties.OrderNo)
                        .list();
                final List<SelectObject> subDataList = new ArrayList<>();
                for (Equipment equipment : equipmentList) {
                    SelectObject selectObject = new SelectObject();
                    selectObject.objectId = equipment.getObjectId();
                    selectObject.value = equipment.getName();
                    subDataList.add(selectObject);
                }
                return subDataList;
            }

            @Override
            public List<SelectObject> getSearchResultDataList(String query) {
                List<SelectObject> searchResultDataList = new ArrayList<>();
                List<Equipment> equipmentList = SimpleApplication.getDaoSession().getEquipmentDao().queryBuilder()
                        .where(EquipmentDao.Properties.FullName.like("%" + query + "%"))
                        .orderAsc(EquipmentDao.Properties.OrderNo)
                        .list();
                for (Equipment equipment : equipmentList) {
                    SelectObject selectObject = new SelectObject();
                    selectObject.objectId = equipment.getObjectId();
                    selectObject.value = equipment.getFullName();
                    searchResultDataList.add(selectObject);
                }
                return searchResultDataList;
            }

        };
    }

    public static GroupSelectActivity.DataProvider getDepartmentDataProvider() {

        return new GroupSelectActivity.DataProvider() {

            private List<SelectObject> mainDataList;

            @Override
            public String getMainId(String subId) {
                Department department = SimpleApplication.getDaoSession().getDepartmentDao().queryBuilder()
                        .where(DepartmentDao.Properties.ObjectId.eq(subId))
                        .unique();
                return department.getParent().getParentId();
            }

            @Override
            public List<SelectObject> getMainDataList() {
                if (mainDataList != null) {
                    return mainDataList;
                }

                final List<Department> buildingList = SimpleApplication.getDaoSession().getDepartmentDao().queryBuilder()
                        .where(DepartmentDao.Properties.Level.eq(0))
                        .orderAsc(DepartmentDao.Properties.OrderNo)
                        .list();
                List<SelectObject> mainDataList = new ArrayList<>();
                for (Department department : buildingList) {
                    SelectObject selectObject = new SelectObject();
                    selectObject.objectId = department.getObjectId();
                    selectObject.value = department.getName();
                    mainDataList.add(selectObject);
                }
                return (this.mainDataList = mainDataList);
            }

            @Override
            public List<SelectObject> getSubDataList(String mainId) {
                List<Department> level1List = SimpleApplication.getDaoSession().getDepartmentDao().queryBuilder()
                        .where(DepartmentDao.Properties.ParentId.eq(mainId))
                        .orderAsc(DepartmentDao.Properties.OrderNo)
                        .list();
                final List<SelectObject> subDataList = new ArrayList<>();
                for (Department level1 : level1List) {
                    for (Department level2 : level1.getChildren()) {
                        SelectObject selectObject = new SelectObject();
                        selectObject.objectId = level2.getObjectId();
                        selectObject.value = level1.getName() + " / " + level2.getName();
                        subDataList.add(selectObject);
                    }
                }
                return subDataList;
            }

            @Override
            public List<SelectObject> getSearchResultDataList(String query) {
                List<SelectObject> resultDataList = new ArrayList<>();
                List<Department> level2List = SimpleApplication.getDaoSession().getDepartmentDao().queryBuilder()
                        .where(DepartmentDao.Properties.Level.eq(2))
                        .where(DepartmentDao.Properties.FullName.like("%" + query + "%"))
                        .orderAsc(DepartmentDao.Properties.OrderNo)
                        .list();
                for (Department level2 : level2List) {
                    SelectObject selectObject = new SelectObject();
                    selectObject.objectId = level2.getObjectId();
                    selectObject.value = level2.getFullName();
                    resultDataList.add(selectObject);
                }
                return resultDataList;
            }
        };
    }

    //


    public static SelectAdapter.DataProvider getProcessModeDataProvider() {
        return new SelectAdapter.DataProvider() {
            @Override
            public List<SelectObject> getDataList() {
                ProcessingModeDao processingModeDao = SimpleApplication.getDaoSession().getProcessingModeDao();
                List<ProcessingMode> processingModeList = processingModeDao.queryBuilder()
                        .orderAsc(ProcessingModeDao.Properties.OrderNo)
                        .list();
                List<SelectObject> dataList = new ArrayList<>();
                for (ProcessingMode processingMode : processingModeList) {
                    SelectObject selectObject = new SelectObject();
                    selectObject.objectId = processingMode.getObjectId();
                    selectObject.value = processingMode.getName();
                    dataList.add(selectObject);
                }
                return dataList;
            }
        };
    }

    public static SelectAdapter.DataProvider getProcessTimeLimitDataProvider() {
        return new SelectAdapter.DataProvider() {
            @Override
            public List<SelectObject> getDataList() {
                ProcessingTimeLimitDao processingModeDao = SimpleApplication.getDaoSession().getProcessingTimeLimitDao();
                List<ProcessingTimeLimit> processingTimeLimitList = processingModeDao.queryBuilder()
                        .orderAsc(ProcessingTimeLimitDao.Properties.OrderNo)
                        .list();
                List<SelectObject> dataList = new ArrayList<>();
                for (ProcessingTimeLimit processingTimeLimit : processingTimeLimitList) {
                    SelectObject selectObject = new SelectObject();
                    selectObject.objectId = processingTimeLimit.getObjectId();
                    selectObject.value = processingTimeLimit.getName();
                    dataList.add(selectObject);
                }
                return dataList;
            }
        };
    }

    public static SelectAdapter.DataProvider getEmergencyDegreeDataProvider() {
        return new SelectAdapter.DataProvider() {
            @Override
            public List<SelectObject> getDataList() {
                EmergencyDegreeDao processingModeDao = SimpleApplication.getDaoSession().getEmergencyDegreeDao();
                List<EmergencyDegree> emergencyDegreeList = processingModeDao.queryBuilder()
                        .orderAsc(EmergencyDegreeDao.Properties.OrderNo)
                        .list();
                List<SelectObject> dataList = new ArrayList<>();
                for (EmergencyDegree dataProvider : emergencyDegreeList) {
                    SelectObject selectObject = new SelectObject();
                    selectObject.objectId = dataProvider.getObjectId();
                    selectObject.value = dataProvider.getName();
                    dataList.add(selectObject);
                }
                return dataList;
            }
        };
    }

    //

    public static String getValue(SelectAdapter.DataProvider dataProvider, String objectIdSelected) {
        for (SelectObject selectObject : dataProvider.getDataList()) {
            if (TextUtils.equals(selectObject.objectId, objectIdSelected)) {
                return selectObject.value;
            }
        }
        return "";
    }

}
