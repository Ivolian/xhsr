package unicorn.com.xhsr.data;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import unicorn.com.xhsr.SimpleApplication;
import unicorn.com.xhsr.data.greendao.Building;
import unicorn.com.xhsr.data.greendao.Department;
import unicorn.com.xhsr.data.greendao.EmergencyDegree;
import unicorn.com.xhsr.data.greendao.Equipment;
import unicorn.com.xhsr.data.greendao.EquipmentCategory;
import unicorn.com.xhsr.data.greendao.ProcessingMode;
import unicorn.com.xhsr.data.greendao.ProcessingTimeLimit;
import unicorn.com.xhsr.data.greendao.SatisfactionOption;
import unicorn.com.xhsr.utils.ConfigUtils;
import unicorn.com.xhsr.utils.ToastUtils;
import unicorn.com.xhsr.volley.SimpleVolley;
import unicorn.com.xhsr.volley.StringRequestWithSessionCheck;


public class BasicDataGotter {

    public void getProcessMode() {
        String url = ConfigUtils.getBaseUrl() + "/api/v1/hems/workOrder/code/ProcessingMode";
        StringRequest jsonArrayRequest = new StringRequestWithSessionCheck(url,
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
                            SimpleApplication.getDaoSession().getProcessingModeDao().deleteAll();
                            SimpleApplication.getDaoSession().getProcessingModeDao().insertInTx(processingModeList);
                        } catch (Exception e) {
                            //
                        }
                    }
                },
                SimpleVolley.getDefaultErrorListener()
        );
        SimpleVolley.addRequest(jsonArrayRequest);
    }

    public void getProcessTimeLimit() {
        String url = ConfigUtils.getBaseUrl() + "/api/v1/hems/workOrder/code/ProcessingTimeLimit";
        StringRequest jsonArrayRequest = new StringRequestWithSessionCheck(url,
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
                            SimpleApplication.getDaoSession().getProcessingTimeLimitDao().deleteAll();
                            SimpleApplication.getDaoSession().getProcessingTimeLimitDao().insertInTx(processingTimeLimitList);
                        } catch (Exception e) {
                            //
                        }
                    }
                },
                SimpleVolley.getDefaultErrorListener()
        );
        SimpleVolley.addRequest(jsonArrayRequest);
    }

    public void getEmergencyDegree() {
        String url = ConfigUtils.getBaseUrl() + "/api/v1/hems/workOrder/code/EmergencyDegree";
        StringRequest jsonArrayRequest = new StringRequestWithSessionCheck(url,
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
                            SimpleApplication.getDaoSession().getEmergencyDegreeDao().deleteAll();
                            SimpleApplication.getDaoSession().getEmergencyDegreeDao().insertInTx(emergencyDegreeList);
                        } catch (Exception e) {
                            //
                        }

                    }
                },
                SimpleVolley.getDefaultErrorListener()
        );
        SimpleVolley.addRequest(jsonArrayRequest);
    }
    //

    public void getEquipment() {
        String url = ConfigUtils.getBaseUrl() + "/api/v1/hems/equipment/all";
        StringRequest jsonArrayRequest = new StringRequestWithSessionCheck(url,
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
                            SimpleApplication.getDaoSession().getEquipmentCategoryDao().deleteAll();
                            SimpleApplication.getDaoSession().getEquipmentCategoryDao().insertInTx(equipmentCategoryList);

                            List<Equipment> equipmentList = new ArrayList<>();
                            for (int i = 0; i != response.length(); i++) {
                                JSONObject categoryObject = response.getJSONObject(i);
                                String categoryId = i + "";
                                String categoryName = categoryObject.getString("name");
                                JSONArray items = categoryObject.getJSONArray("items");
                                for (int j = 0; j != items.length(); j++) {
                                    JSONObject equipmentObject = items.getJSONObject(j);
                                    Equipment equipment = new Equipment();
                                    equipment.setObjectId(equipmentObject.getString("objectId"));
                                    equipment.setName(equipmentObject.getString("name"));
                                    equipment.setFullName(categoryName + " / " + equipment.getName());
                                    equipment.setOrderNo(j);
                                    equipment.setCategoryId(categoryId);
                                    equipmentList.add(equipment);
                                }
                            }
                            SimpleApplication.getDaoSession().getEquipmentDao().deleteAll();
                            SimpleApplication.getDaoSession().getEquipmentDao().insertInTx(equipmentList);
                        } catch (Exception e) {
                            //
                        }
                    }
                },
                SimpleVolley.getDefaultErrorListener()
        );
        SimpleVolley.addRequest(jsonArrayRequest);
    }

    public void getBuilding() {
        String url = ConfigUtils.getBaseUrl() + "/api/v1/hems/building/tree?id=1&fetchChild=true";
        StringRequest jsonArrayRequest = new StringRequestWithSessionCheck(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String responses) {
                        try {
                            JSONArray response = new JSONArray(responses);
                            List<Building> buildingList = new ArrayList<>();
                            for (int i = 0; i != response.length(); i++) {
                                JSONObject fatherObject = response.getJSONObject(i);
                                String fatherId = fatherObject.getString("id");
                                Building father = new Building();
                                father.setParentId("root");
                                father.setLevel(0);
                                father.setObjectId(fatherId);
                                father.setName(fatherObject.getString("name"));
                                father.setFullName(father.getName());
                                father.setOrderNo(fatherObject.getInt("orderNo"));
                                buildingList.add(father);

                                JSONArray items = fatherObject.getJSONArray("items");
                                for (int j = 0; j != items.length(); j++) {
                                    JSONObject childObject = items.getJSONObject(j);
                                    Building child = new Building();
                                    child.setParentId(fatherId);
                                    child.setLevel(1);
                                    child.setObjectId(childObject.getString("id"));
                                    child.setName(childObject.getString("name"));
                                    child.setFullName(father.getName() + " / " + child.getName());
                                    child.setOrderNo(childObject.getInt("orderNo"));
                                    buildingList.add(child);
                                }
                            }
                            SimpleApplication.getDaoSession().getBuildingDao().deleteAll();
                            SimpleApplication.getDaoSession().getBuildingDao().insertInTx(buildingList);
                        } catch (Exception e) {
                            ToastUtils.show(e.getMessage());
                        }
                    }
                },
                SimpleVolley.getDefaultErrorListener()
        );
        SimpleVolley.addRequest(jsonArrayRequest);
    }

    public void getDepartment() {
        String url = ConfigUtils.getBaseUrl() + "/api/v1/hems/department/tree?id=1&fetchChild=true";
        StringRequest jsonArrayRequest = new StringRequestWithSessionCheck(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String responses) {
                        try {
                            JSONArray response = new JSONArray(responses);
                            List<Department> departmentList = new ArrayList<>();
                            for (int i = 0; i != response.length(); i++) {
                                JSONObject level0Object = response.getJSONObject(i);
                                String level0Id = level0Object.getString("id");
                                Department level0 = new Department();
                                level0.setParentId("root");
                                level0.setLevel(0);
                                level0.setObjectId(level0Id);
                                level0.setName(level0Object.getString("name"));
                                level0.setFullName(level0.getName());
                                level0.setOrderNo(level0Object.getInt("orderNo"));
                                departmentList.add(level0);

                                JSONArray level0Items = level0Object.getJSONArray("items");
                                for (int j = 0; j != level0Items.length(); j++) {
                                    JSONObject level1Object = level0Items.getJSONObject(j);
                                    String level1Id = level1Object.getString("id");
                                    Department level1 = new Department();
                                    level1.setParentId(level0Id);
                                    level1.setLevel(1);
                                    level1.setObjectId(level1Id);
                                    level1.setName(level1Object.getString("name"));
                                    level1.setFullName(level0.getName() + " / " + level1.getName());
                                    level1.setOrderNo(level1Object.getInt("orderNo"));
                                    departmentList.add(level1);

                                    JSONArray level1Items = level1Object.getJSONArray("items");
                                    for (int k = 0; k != level1Items.length(); k++) {
                                        JSONObject level2Object = level1Items.getJSONObject(k);
                                        String level2Id = level2Object.getString("id");
                                        Department level2 = new Department();
                                        level2.setParentId(level1Id);
                                        level2.setLevel(2);
                                        level2.setObjectId(level2Id);
                                        level2.setName(level2Object.getString("name"));
                                        level2.setFullName(level0.getName() + " / " + level1.getName() + " / " + level2.getName());
                                        level2.setOrderNo(level2Object.getInt("orderNo"));
                                        departmentList.add(level2);
                                    }
                                }
                            }
                            SimpleApplication.getDaoSession().getDepartmentDao().deleteAll();
                            SimpleApplication.getDaoSession().getDepartmentDao().insertInTx(departmentList);
                        } catch (Exception e) {
                            //
                        }
                    }
                },
                SimpleVolley.getDefaultErrorListener()
        );
        SimpleVolley.addRequest(jsonArrayRequest);
    }

    public void getOption() {
        String url = ConfigUtils.getBaseUrl() + "/api/v1/hems/satisfactionContent/list";
        StringRequest jsonArrayRequest = new StringRequestWithSessionCheck(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String responses) {
                        try {
                            JSONArray response = new JSONArray(responses);
                            List<SatisfactionOption> optionList = new ArrayList<>();
                            int orderNo = 0;
                            for (int i = 0; i != response.length(); i++) {
                                JSONObject serviceObject = response.getJSONObject(i);
                                String serviceName = (i + 1) + ". " + serviceObject.getString("name");
                                JSONArray items = serviceObject.getJSONArray("items");
                                for (int j = 0; j != items.length(); j++) {
                                    SatisfactionOption option = new SatisfactionOption();
                                    option.setTitle(serviceName);
                                    option.setOrderNo(orderNo++);
                                    option.setScore(-1);
                                    option.setNumerator(j + 1);
                                    option.setDenominator(items.length());

                                    JSONObject itemObject = items.getJSONObject(j);
                                    String objectId = itemObject.getString("objectId");
                                    String content = (i + 1) + "." + (j + 1) + " " + itemObject.getString("name");
                                    option.setObjectId(objectId);
                                    option.setContent(content);
                                    optionList.add(option);
                                }
                            }
                            SimpleApplication.getDaoSession().getSatisfactionOptionDao().deleteAll();
                            SimpleApplication.getDaoSession().getSatisfactionOptionDao().insertInTx(optionList);
                        } catch (Exception e) {
                            //
                        }
                    }
                },
                SimpleVolley.getDefaultErrorListener()
        );
        SimpleVolley.addRequest(jsonArrayRequest);
    }

}
