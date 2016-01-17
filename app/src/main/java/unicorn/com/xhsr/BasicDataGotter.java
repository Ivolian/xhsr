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

import unicorn.com.xhsr.greendao.Equipment;
import unicorn.com.xhsr.greendao.EquipmentCategory;
import unicorn.com.xhsr.greendao.ProcessingMode;
import unicorn.com.xhsr.greendao.ProcessingModeDao;
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

                            ProcessingModeDao processingModeDao = SimpleApplication.getProcessingModeDao();
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
                map.put("Cookie", "JSESSIONID=" + "CE46D6C7FD5F1D2AD08B2CD3F6D80CB1");
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
                            SimpleApplication.getEquipmentCategoryDao().deleteAll();
                            SimpleApplication.getEquipmentCategoryDao().insertInTx(equipmentCategoryList);


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
                            SimpleApplication.getEquipmentDao().deleteAll();
                            SimpleApplication.getEquipmentDao().insertInTx(equipmentList);


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
                map.put("Cookie", "JSESSIONID=" + "CE46D6C7FD5F1D2AD08B2CD3F6D80CB1");
                return map;
            }
        };
        SimpleVolley.addRequest(jsonArrayRequest);
    }


}
