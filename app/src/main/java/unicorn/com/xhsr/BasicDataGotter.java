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

import unicorn.com.xhsr.greendao.ProcessingMode;
import unicorn.com.xhsr.greendao.ProcessingModeDao;
import unicorn.com.xhsr.utils.ToastUtils;

/**
 * Created by Administrator on 2016/1/17.
 */
public class BasicDataGotter {

    public void getData() {

        String url = "http://withub.net.cn/hems/api/v1/hems/workOrder/code/ProcessingMode";
        StringRequest jsonArrayRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String responses) {
                     ToastUtils.show("hehe");
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
                        ToastUtils.show("3232");
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("Cookie", "JSESSIONID=" +"CE46D6C7FD5F1D2AD08B2CD3F6D80CB1");
                return map;
            }
        };
        SimpleVolley.addRequest(jsonArrayRequest);
    }


}
