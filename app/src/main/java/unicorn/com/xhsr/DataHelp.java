package unicorn.com.xhsr;

import java.util.ArrayList;
import java.util.List;

import unicorn.com.xhsr.greendao.Building;
import unicorn.com.xhsr.greendao.BuildingDao;
import unicorn.com.xhsr.greendao.Department;
import unicorn.com.xhsr.greendao.DepartmentCategory;
import unicorn.com.xhsr.greendao.DepartmentCategoryDao;
import unicorn.com.xhsr.greendao.DepartmentDao;
import unicorn.com.xhsr.greendao.Equipment;
import unicorn.com.xhsr.greendao.EquipmentCategory;
import unicorn.com.xhsr.greendao.EquipmentCategoryDao;
import unicorn.com.xhsr.greendao.EquipmentDao;
import unicorn.com.xhsr.greendao.Floor;
import unicorn.com.xhsr.greendao.FloorDao;
import unicorn.com.xhsr.groupselect.GroupSelectActivity;
import unicorn.com.xhsr.select.SelectObject;


public class DataHelp {

    public static GroupSelectActivity.DataProvider getFloorDataProvider() {

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
            public List<SelectObject> getSubDataList(String buildingId) {
                FloorDao floorDao = SimpleApplication.getDaoSession().getFloorDao();
                List<Floor> floorList = floorDao.queryBuilder()
                        .where(FloorDao.Properties.BuildingId.eq(buildingId))
                        .orderAsc(FloorDao.Properties.OrderNo)
                        .list();
                final List<SelectObject> selectObjectList = new ArrayList<>();
                for (Floor floor : floorList) {
                    SelectObject selectObject = new SelectObject();
                    selectObject.objectId = floor.getObjectId();
                    selectObject.value = floor.getName();
                    selectObjectList.add(selectObject);
                }
                return selectObjectList;
            }

            @Override
            public List<SelectObject> getSearchResultDataList(String query) {

                // 过滤建筑
                List<SelectObject> selectObjectList = new ArrayList<>();
                BuildingDao buildingDao = SimpleApplication.getDaoSession().getBuildingDao();
                List<Building> buildingList = buildingDao.queryBuilder()
                        .where(BuildingDao.Properties.Name.like("%" + query + "%"))
                        .orderAsc(BuildingDao.Properties.OrderNo)
                        .list();
                for (Building building : buildingList) {
                    for (Floor floor : building.getFloorList()) {
                        SelectObject selectObject = new SelectObject();
                        selectObject.objectId = floor.getObjectId();
                        selectObject.value = building.getName() + " / " + floor.getName();
                        selectObjectList.add(selectObject);
                    }
                }

                // 过滤楼层
                FloorDao floorDao = SimpleApplication.getDaoSession().getFloorDao();
                List<Floor> floorList = floorDao.queryBuilder()
                        .where(FloorDao.Properties.Name.like("%" + query + "%"))
                        .orderAsc(FloorDao.Properties.OrderNo)
                        .list();
                for (Floor floor : floorList) {
                    SelectObject selectObject = new SelectObject();
                    selectObject.objectId = floor.getObjectId();
                    selectObject.value = floor.getName();
                    selectObjectList.add(selectObject);
                    if (!selectObjectList.contains(selectObject)) {
                        selectObjectList.add(selectObject);
                    }
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

                // 过滤设备目录
                List<SelectObject> selectObjectList = new ArrayList<>();
                EquipmentCategoryDao equipmentCategoryDao = SimpleApplication.getDaoSession().getEquipmentCategoryDao();
                List<EquipmentCategory> equipmentCategoryList = equipmentCategoryDao.queryBuilder()
                        .where(EquipmentCategoryDao.Properties.Name.like("%" + query + "%"))
                        .orderAsc(EquipmentCategoryDao.Properties.OrderNo)
                        .list();
                for (EquipmentCategory equipmentCategory : equipmentCategoryList) {
                    for (Equipment equipment : equipmentCategory.getEquipmentList()) {
                        SelectObject selectObject = new SelectObject();
                        selectObject.objectId = equipment.getObjectId();
                        selectObject.value = equipmentCategory.getName() + " / " + equipment.getName();
                        selectObjectList.add(selectObject);
                    }
                }

                // 过滤设备
                EquipmentDao equipmentDao = SimpleApplication.getDaoSession().getEquipmentDao();
                List<Equipment> equipmentList = equipmentDao.queryBuilder()
                        .where(EquipmentDao.Properties.Name.like("%" + query + "%"))
                        .orderAsc(EquipmentDao.Properties.OrderNo)
                        .list();
                for (Equipment equipment : equipmentList) {
                        SelectObject selectObject = new SelectObject();
                        selectObject.objectId = equipment.getObjectId();
                        selectObject.value = equipment.getName();
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
                DepartmentCategoryDao departmentCategoryDao = SimpleApplication.getDaoSession().getDepartmentCategoryDao();
                final List<DepartmentCategory> departmentCategoryList = departmentCategoryDao.queryBuilder()
                        .orderAsc(DepartmentCategoryDao.Properties.OrderNo)
                        .list();
                List<SelectObject> selectObjectList = new ArrayList<>();
                for (DepartmentCategory departmentCategory : departmentCategoryList) {
                    SelectObject selectObject = new SelectObject();
                    selectObject.objectId = departmentCategory.getObjectId();
                    selectObject.value = departmentCategory.getName();
                    selectObjectList.add(selectObject);
                }
                return selectObjectList;
            }

            @Override
            public List<SelectObject> getSubDataList(String categoryId) {
                DepartmentDao departmentDao = SimpleApplication.getDaoSession().getDepartmentDao();
                List<Department> departmentList = departmentDao.queryBuilder()
                        .where(DepartmentDao.Properties.CategoryId.eq(categoryId))
                        .orderAsc(DepartmentDao.Properties.OrderNo)
                        .list();
                final List<SelectObject> selectObjectList = new ArrayList<>();
                for (Department department : departmentList) {
                    SelectObject selectObject = new SelectObject();
                    selectObject.objectId = department.getObjectId();
                    selectObject.value = department.getName();
                    selectObjectList.add(selectObject);
                }
                return selectObjectList;
            }

            @Override
            public List<SelectObject> getSearchResultDataList(String query) {

                // 过滤设备目录
                List<SelectObject> selectObjectList = new ArrayList<>();
                DepartmentCategoryDao equipmentCategoryDao = SimpleApplication.getDaoSession().getDepartmentCategoryDao();
                List<DepartmentCategory> departmentCategoryList = equipmentCategoryDao.queryBuilder()
                        .where(DepartmentCategoryDao.Properties.Name.like("%" + query + "%"))
                        .orderAsc(DepartmentCategoryDao.Properties.OrderNo)
                        .list();
                for (DepartmentCategory departmentCategory : departmentCategoryList) {
                    for (Department department : departmentCategory.getDepartmentList()) {
                        SelectObject selectObject = new SelectObject();
                        selectObject.objectId = department.getObjectId();
                        selectObject.value = departmentCategory.getName() + " / " + department.getName();
                        selectObjectList.add(selectObject);
                    }
                }

                // 过滤设备
                DepartmentDao departmentDao = SimpleApplication.getDaoSession().getDepartmentDao();
                List<Department> departmentList = departmentDao.queryBuilder()
                        .where(DepartmentDao.Properties.Name.like("%" + query + "%"))
                        .orderAsc(DepartmentDao.Properties.OrderNo)
                        .list();
                for (Department department : departmentList) {
                    SelectObject selectObject = new SelectObject();
                    selectObject.objectId = department.getObjectId();
                    selectObject.value = department.getName();
                    selectObjectList.add(selectObject);
                    // todo
                }
                return selectObjectList;
            }
        };
    }


    public static boolean wait_repair = false;

}
