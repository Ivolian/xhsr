package unicorn.com.xhsr.data;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import unicorn.com.xhsr.SimpleApplication;
import unicorn.com.xhsr.data.greendao.Building;
import unicorn.com.xhsr.data.greendao.BuildingDao;
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
            @Override
            public List<SelectObject> getMainDataList() {
                BuildingDao buildingDao = SimpleApplication.getDaoSession().getBuildingDao();
                final List<Building> buildingList = buildingDao.queryBuilder()
                        .where(BuildingDao.Properties.ParentId.eq("root"))
                        .orderAsc(BuildingDao.Properties.OrderNo)
                        .list();
                List<SelectObject> selectObjectList = new ArrayList<>();
                for (Building building : buildingList) {
                    SelectObject selectObject = new SelectObject();
                    selectObject.objectId = building.getObjectId();
                    selectObject.value = building.getName();
                    selectObjectList.add(selectObject);
                }
                return selectObjectList;
            }

            @Override
            public List<SelectObject> getSubDataList(String parentId) {
                BuildingDao buildingDao = SimpleApplication.getDaoSession().getBuildingDao();
                List<Building> equipmentList = buildingDao.queryBuilder()
                        .where(BuildingDao.Properties.ParentId.eq(parentId))
                        .orderAsc(BuildingDao.Properties.OrderNo)
                        .list();
                final List<SelectObject> selectObjectList = new ArrayList<>();
                for (Building building : equipmentList) {
                    SelectObject selectObject = new SelectObject();
                    selectObject.objectId = building.getObjectId();
                    selectObject.value = building.getName();
                    selectObjectList.add(selectObject);
                }
                return selectObjectList;
            }

            @Override
            public List<SelectObject> getSearchResultDataList(String query) {
                List<SelectObject> selectObjectList = new ArrayList<>();
                BuildingDao buildingDao = SimpleApplication.getDaoSession().getBuildingDao();
                List<Building> children = buildingDao.queryBuilder()
                        .where(BuildingDao.Properties.Level.eq(1))
                        .where(BuildingDao.Properties.FullName.like("%" + query + "%"))
                        .orderAsc(BuildingDao.Properties.OrderNo)
                        .list();
                for (Building child : children) {
                    SelectObject selectObject = new SelectObject();
                    selectObject.objectId = child.getObjectId();
                    selectObject.value = child.getFullName();
                    selectObjectList.add(selectObject);
                }
                return selectObjectList;
            }
        };

    }

    public static GroupSelectActivity.DataProvider getEquipmentDataProvider() {

        return new GroupSelectActivity.DataProvider() {
            @Override
            public List<SelectObject> getMainDataList() {
                EquipmentCategoryDao equipmentCategoryDao = SimpleApplication.getDaoSession().getEquipmentCategoryDao();
                final List<EquipmentCategory> equipmentCategoryList = equipmentCategoryDao.queryBuilder()
                        .orderAsc(EquipmentCategoryDao.Properties.OrderNo)
                        .list();
                List<SelectObject> selectObjectList = new ArrayList<>();
                for (EquipmentCategory equipmentCategory : equipmentCategoryList) {
                    SelectObject selectObject = new SelectObject();
                    selectObject.objectId = equipmentCategory.getObjectId();
                    selectObject.value = equipmentCategory.getName();
                    selectObjectList.add(selectObject);
                }
                return selectObjectList;
            }

            @Override
            public List<SelectObject> getSubDataList(String categoryId) {
                EquipmentDao equipmentDao = SimpleApplication.getDaoSession().getEquipmentDao();
                List<Equipment> equipmentList = equipmentDao.queryBuilder()
                        .where(EquipmentDao.Properties.CategoryId.eq(categoryId))
                        .orderAsc(EquipmentDao.Properties.OrderNo)
                        .list();
                final List<SelectObject> selectObjectList = new ArrayList<>();
                for (Equipment equipment : equipmentList) {
                    SelectObject selectObject = new SelectObject();
                    selectObject.objectId = equipment.getObjectId();
                    selectObject.value = equipment.getName();
                    selectObjectList.add(selectObject);
                }
                return selectObjectList;
            }

            @Override
            public List<SelectObject> getSearchResultDataList(String query) {
                List<SelectObject> selectObjectList = new ArrayList<>();
                EquipmentDao equipmentDao = SimpleApplication.getDaoSession().getEquipmentDao();
                List<Equipment> equipmentList = equipmentDao.queryBuilder()
                        .where(EquipmentDao.Properties.FullName.like("%" + query + "%"))
                        .orderAsc(EquipmentDao.Properties.OrderNo)
                        .list();
                for (Equipment equipment : equipmentList) {
                    SelectObject selectObject = new SelectObject();
                    selectObject.objectId = equipment.getObjectId();
                    selectObject.value = equipment.getFullName();
                    selectObjectList.add(selectObject);
                }
                return selectObjectList;
            }
        };
    }

    public static GroupSelectActivity.DataProvider getDepartmentDataProvider() {

        return new GroupSelectActivity.DataProvider() {
            @Override
            public List<SelectObject> getMainDataList() {
                BuildingDao buildingDao = SimpleApplication.getDaoSession().getBuildingDao();
                final List<Building> buildingList = buildingDao.queryBuilder()
                        .orderAsc(BuildingDao.Properties.OrderNo)
                        .list();
                List<SelectObject> selectObjectList = new ArrayList<>();
                for (Building building : buildingList) {
                    SelectObject selectObject = new SelectObject();
                    selectObject.objectId = building.getObjectId();
                    selectObject.value = building.getName();
                    selectObjectList.add(selectObject);
                }
                return selectObjectList;
            }

            @Override
            public List<SelectObject> getSubDataList(String fatherId) {

                return null;
            }

            @Override
            public List<SelectObject> getSearchResultDataList(String query) {

//                // 过滤设备目录
//                List<SelectObject> selectObjectList = new ArrayList<>();
//                DepartmentCategoryDao equipmentCategoryDao = SimpleApplication.getDaoSession().getDepartmentCategoryDao();
//                List<DepartmentCategory> departmentCategoryList = equipmentCategoryDao.queryBuilder()
//                        .where(DepartmentCategoryDao.Properties.Name.like("%" + query + "%"))
//                        .orderAsc(DepartmentCategoryDao.Properties.OrderNo)
//                        .list();
//                for (DepartmentCategory departmentCategory : departmentCategoryList) {
//                    for (Department department : departmentCategory.getDepartmentList()) {
//                        SelectObject selectObject = new SelectObject();
//                        selectObject.objectId = department.getObjectId();
//                        selectObject.value = departmentCategory.getName() + " / " + department.getName();
//                        selectObjectList.add(selectObject);
//                    }
//                }
//
//                // 过滤设备
//                DepartmentDao departmentDao = SimpleApplication.getDaoSession().getDepartmentDao();
//                List<Department> departmentList = departmentDao.queryBuilder()
//                        .where(DepartmentDao.Properties.Name.like("%" + query + "%"))
//                        .orderAsc(DepartmentDao.Properties.OrderNo)
//                        .list();
//                for (Department department : departmentList) {
//                    SelectObject selectObject = new SelectObject();
//                    selectObject.objectId = department.getObjectId();
//                    selectObject.value = department.getName();
//                    selectObjectList.add(selectObject);
//                    // todo
//                }
                return null;
            }
        };
    }

    //

    public static boolean wait_repair = false;

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
