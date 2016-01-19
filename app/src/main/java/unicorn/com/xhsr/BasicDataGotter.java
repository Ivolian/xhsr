package unicorn.com.xhsr;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import unicorn.com.xhsr.greendao.Building;
import unicorn.com.xhsr.greendao.Department;
import unicorn.com.xhsr.greendao.DepartmentCategory;
import unicorn.com.xhsr.greendao.EmergencyDegree;
import unicorn.com.xhsr.greendao.EmergencyDegreeDao;
import unicorn.com.xhsr.greendao.Equipment;
import unicorn.com.xhsr.greendao.EquipmentCategory;
import unicorn.com.xhsr.greendao.Floor;
import unicorn.com.xhsr.greendao.ProcessingMode;
import unicorn.com.xhsr.greendao.ProcessingModeDao;
import unicorn.com.xhsr.greendao.ProcessingTimeLimit;
import unicorn.com.xhsr.greendao.ProcessingTimeLimitDao;
import unicorn.com.xhsr.utils.ToastUtils;

/**
 * Created by Administrator on 2016/1/17.
 */
public class BasicDataGotter {

    public void getProcessMode() {

        String url = "http://withub.net.cn/hems/api/v1/hems/workOrder/code/ProcessingMode";
        StringRequest jsonArrayRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String responses) {
                        try {
                            JSONArray response = new JSONArray(responses);
                            List<ProcessingMode> processingModeList = new ArrayList<>();
                            for (int i = 0; i != response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                ProcessingMode processingMode = new ProcessingMode();
                                processingMode.setObjectId(jsonObject.getString("objectId"));
                                processingMode.setName(jsonObject.getString("name"));
                                processingMode.setOrderNo(i);
                                processingModeList.add(processingMode);
                            }

                            ProcessingModeDao processingModeDao = SimpleApplication.getDaoSession().getProcessingModeDao();
                            processingModeDao.deleteAll();
                            processingModeDao.insertInTx(processingModeList);
                        } catch (Exception e) {

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("Cookie", "JSESSIONID=" +ConfigUtils.SESSION_ID);
                return map;
            }
        };
        SimpleVolley.addRequest(jsonArrayRequest);
    }


    public void getProcessTimeLimit() {
        String url = "http://withub.net.cn/hems/api/v1/hems/workOrder/code/ProcessingTimeLimit";
        StringRequest jsonArrayRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String responses) {
                        try {
                            JSONArray response = new JSONArray(responses);
                            List<ProcessingTimeLimit> processingTimeLimitList = new ArrayList<>();
                            for (int i = 0; i != response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                ProcessingTimeLimit processingTimeLimit = new ProcessingTimeLimit();
                                processingTimeLimit.setObjectId(jsonObject.getString("objectId"));
                                processingTimeLimit.setName(jsonObject.getString("name"));
                                processingTimeLimit.setOrderNo(i);
                                processingTimeLimitList.add(processingTimeLimit);
                            }

                            ProcessingTimeLimitDao processingTimeLimitDao = SimpleApplication.getDaoSession().getProcessingTimeLimitDao();
                            processingTimeLimitDao.deleteAll();
                            processingTimeLimitDao.insertInTx(processingTimeLimitList);
                        } catch (Exception e) {

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("Cookie", "JSESSIONID=" +ConfigUtils.SESSION_ID);
                return map;
            }
        };
        SimpleVolley.addRequest(jsonArrayRequest);
    }


    public void getEmergencyDegree() {

        String url = "http://withub.net.cn/hems/api/v1/hems/workOrder/code/EmergencyDegree";
        StringRequest jsonArrayRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String responses) {
                        try {
                            JSONArray response = new JSONArray(responses);
                            List<EmergencyDegree> emergencyDegreeList = new ArrayList<>();
                            for (int i = 0; i != response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                EmergencyDegree emergencyDegree = new EmergencyDegree();
                                emergencyDegree.setObjectId(jsonObject.getString("objectId"));
                                emergencyDegree.setName(jsonObject.getString("name"));
                                emergencyDegree.setOrderNo(i);
                                emergencyDegreeList.add(emergencyDegree);
                            }

                            EmergencyDegreeDao emergencyDegreeDao = SimpleApplication.getDaoSession().getEmergencyDegreeDao();
                            emergencyDegreeDao.deleteAll();
                            emergencyDegreeDao.insertInTx(emergencyDegreeList);
                        } catch (Exception e) {

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("Cookie", "JSESSIONID=" +ConfigUtils.SESSION_ID);
                return map;
            }
        };
        SimpleVolley.addRequest(jsonArrayRequest);
    }

    public void getEquipment() {

        String url = "http://withub.net.cn/hems/api/v1/hems/equipment/all";
        StringRequest jsonArrayRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String responses) {

                        try {
                            JSONArray response = new JSONArray(responses);


                            List<EquipmentCategory> equipmentCategoryList = new ArrayList<>();
                            for (int i = 0; i != response.length(); i++) {
                                JSONObject categoryJSONObject = response.getJSONObject(i);
                                EquipmentCategory equipmentCategory = new EquipmentCategory();
                                equipmentCategory.setObjectId(i + "");
                                equipmentCategory.setName(categoryJSONObject.getString("name"));
                                equipmentCategory.setOrderNo(i);
                                equipmentCategoryList.add(equipmentCategory);
                            }

                            EquipmentCategory equipmentCategory = new EquipmentCategory();
                            equipmentCategory.setObjectId("root");
                            equipmentCategory.setName("常用设备");
                            equipmentCategory.setOrderNo(-1);
                            equipmentCategoryList.add(equipmentCategory);

                            SimpleApplication.getDaoSession().getEquipmentCategoryDao().deleteAll();
                            SimpleApplication.getDaoSession().getEquipmentCategoryDao().insertInTx(equipmentCategoryList);


                            List<Equipment> equipmentList = new ArrayList<>();
                            for (int i = 0; i != response.length(); i++) {
                                JSONObject categoryJSONObject = response.getJSONObject(i);
                                String categoryId = i + "";
                                JSONArray items = categoryJSONObject.getJSONArray("items");
                                for (int j = 0; j != items.length(); j++) {
                                    JSONObject equipmentJSONObject = items.getJSONObject(j);
                                    Equipment equipment = new Equipment();
                                    equipment.setObjectId(equipmentJSONObject.getString("objectId"));
                                    equipment.setName(equipmentJSONObject.getString("name"));
                                    equipment.setOrderNo(j);
                                    equipment.setCategoryId(categoryId);
                                    equipmentList.add(equipment);
                                }
                            }
                            SimpleApplication.getDaoSession().getEquipmentDao().deleteAll();
                            SimpleApplication.getDaoSession().getEquipmentDao().insertInTx(equipmentList);


                        } catch (Exception e) {
                            ToastUtils.show(e.getMessage());
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("Cookie", "JSESSIONID=" + ConfigUtils.SESSION_ID);
                return map;
            }
        };
        SimpleVolley.addRequest(jsonArrayRequest);
    }


    public void getBuildingAndFloor() {

        String url = "http://withub.net.cn/hems/api/v1/hems/building/tree?id=1&fetchChild=true";
        StringRequest jsonArrayRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String responses) {

                        try {
                            JSONArray response = new JSONArray(responses);


                            List<Building> buildingList = new ArrayList<>();
                            for (int i = 0; i != response.length(); i++) {
                                JSONObject buildingJSONObject = response.getJSONObject(i);
                                Building building = new Building();
                                building.setObjectId(i + "");
                                building.setName(buildingJSONObject.getString("name"));
                                building.setOrderNo(i);
                                buildingList.add(building);
                            }
                            SimpleApplication.getDaoSession().getBuildingDao().deleteAll();
                            SimpleApplication.getDaoSession().getBuildingDao().insertInTx(buildingList);


                            List<Floor> floorList = new ArrayList<>();
                            for (int i = 0; i != response.length(); i++) {
                                JSONObject buildingJSONObject = response.getJSONObject(i);
                                String buildingId = i + "";
                                JSONArray items = buildingJSONObject.getJSONArray("items");
                                for (int j = 0; j != items.length(); j++) {
                                    JSONObject floorJSONObject = items.getJSONObject(j);
                                    Floor floor = new Floor();
                                    floor.setObjectId(floorJSONObject.getString("objectId"));
                                    floor.setName(floorJSONObject.getString("name"));
                                    floor.setOrderNo(j);
                                    floor.setBuildingId(buildingId);
                                    floorList.add(floor);
                                }
                            }
                            SimpleApplication.getDaoSession().getFloorDao().deleteAll();
                            SimpleApplication.getDaoSession().getFloorDao().insertInTx(floorList);


                        } catch (Exception e) {
                            ToastUtils.show(e.getMessage());
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("Cookie", "JSESSIONID=" + ConfigUtils.SESSION_ID);
                return map;
            }
        };
        SimpleVolley.addRequest(jsonArrayRequest);
    }


    public void getDepartment() {

        String url = "http://withub.net.cn/hems/api/v1/hems/department/tree?id=1&fetchChild=true";
        StringRequest jsonArrayRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String responses) {

                        try {
                            JSONArray response = new JSONArray(responses);


                            List<DepartmentCategory> departmentCategoryList = new ArrayList<>();
                            for (int i = 0; i != response.length(); i++) {
                                JSONObject categoryJSONObject = response.getJSONObject(i);
                                DepartmentCategory departmentCategory = new DepartmentCategory();
                                departmentCategory.setObjectId(i + "");
                                departmentCategory.setName(categoryJSONObject.getString("name"));
                                departmentCategory.setOrderNo(i);
                                departmentCategoryList.add(departmentCategory);
                            }


                            SimpleApplication.getDaoSession().getDepartmentCategoryDao().deleteAll();
                            SimpleApplication.getDaoSession().getDepartmentCategoryDao().insertInTx(departmentCategoryList);


                            List<Department> departmentList = new ArrayList<>();
                            for (int i = 0; i != response.length(); i++) {
                                JSONObject categoryJSONObject = response.getJSONObject(i);
                                String categoryId = i + "";
                                JSONArray items = categoryJSONObject.getJSONArray("items");
                                for (int j = 0; j != items.length(); j++) {
                                    JSONObject departmentJSONObject = items.getJSONObject(j);
                                    Department department = new Department();
                                    department.setObjectId(departmentJSONObject.getString("objectId"));
                                    department.setName(departmentJSONObject.getString("name"));
                                    department.setOrderNo(j);
                                    department.setCategoryId(categoryId);
                                    departmentList.add(department);
                                }
                            }
                            SimpleApplication.getDaoSession().getDepartmentDao().deleteAll();
                            SimpleApplication.getDaoSession().getDepartmentDao().insertInTx(departmentList);


                        } catch (Exception e) {
                            ToastUtils.show(e.getMessage());
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("Cookie", "JSESSIONID=" + ConfigUtils.SESSION_ID);
                return map;
            }
        };
        SimpleVolley.addRequest(jsonArrayRequest);
    }
}
