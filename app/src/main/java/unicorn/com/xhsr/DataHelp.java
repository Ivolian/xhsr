package unicorn.com.xhsr;

import java.util.ArrayList;
import java.util.List;

import unicorn.com.xhsr.greendao.Building;
import unicorn.com.xhsr.greendao.BuildingDao;
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
                List<SelectObject> resultList = new ArrayList<>();
                List<String> objectIdList = new ArrayList<>();
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
                        resultList.add(selectObject);
                    }
                }

                // 过滤楼层
                FloorDao floorDao = SimpleApplication.getDaoSession().getFloorDao();
                List<Floor> floorList = floorDao.queryBuilder()
                        .where(FloorDao.Properties.Name.like("%" + query + "%"))
                        .orderAsc(FloorDao.Properties.OrderNo)
                        .list();
                for (Floor floor : floorList) {
                    if (objectIdList.contains(floor.getObjectId())) {
                        SelectObject selectObject = new SelectObject();
                        selectObject.objectId = floor.getObjectId();
                        selectObject.value = floor.getName();
                        resultList.add(selectObject);
                    }
                }

                return resultList;
            }
        };
    }

}
