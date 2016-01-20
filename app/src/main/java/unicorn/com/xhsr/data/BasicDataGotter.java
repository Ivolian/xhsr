package unicorn.com.xhsr.data;

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

import unicorn.com.xhsr.ConfigUtils;
import unicorn.com.xhsr.SimpleApplication;
import unicorn.com.xhsr.data.greendao.Building;
import unicorn.com.xhsr.data.greendao.EmergencyDegree;
import unicorn.com.xhsr.data.greendao.Equipment;
import unicorn.com.xhsr.data.greendao.EquipmentCategory;
import unicorn.com.xhsr.data.greendao.ProcessingMode;
import unicorn.com.xhsr.data.greendao.ProcessingTimeLimit;
import unicorn.com.xhsr.utils.ToastUtils;
import unicorn.com.xhsr.volley.SimpleVolley;
import unicorn.com.xhsr.volley.StringRequestWithSessionCheck;


public class BasicDataGotter {


    public void getProcessMode() {
        String url = "http://withub.net.cn/hems/api/v1/hems/workOrder/code/ProcessingMode";
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
        String url = "http://withub.net.cn/hems/api/v1/hems/workOrder/code/ProcessingTimeLimit";
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
        String url = "http://withub.net.cn/hems/api/v1/hems/workOrder/code/EmergencyDegree";
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
        String url = "http://withub.net.cn/hems/api/v1/hems/equipment/all";
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
        String url = "http://withub.net.cn/hems/api/v1/hems/building/tree?id=1&fetchChild=true";
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
        String url = "http://withub.net.cn/hems/api/v1/hems/department/tree?id=1&fetchChild=true";
        StringRequest jsonArrayRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String responses) {
                        try {

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
