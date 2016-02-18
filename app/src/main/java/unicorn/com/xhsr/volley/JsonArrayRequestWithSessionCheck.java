package unicorn.com.xhsr.volley;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import unicorn.com.xhsr.utils.ConfigUtils;


public class JsonArrayRequestWithSessionCheck extends JsonArrayRequest {

    public JsonArrayRequestWithSessionCheck(int method, String url, String requestBody, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        super(method, url, requestBody, listener, errorListener);
    }

    public JsonArrayRequestWithSessionCheck(String url, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        super(url, listener, errorListener);
    }

    public JsonArrayRequestWithSessionCheck(int method, String url, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
    }

    public JsonArrayRequestWithSessionCheck(int method, String url, JSONArray jsonRequest, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
    }

    public JsonArrayRequestWithSessionCheck(int method, String url, JSONObject jsonRequest, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
    }

    public JsonArrayRequestWithSessionCheck(String url, JSONArray jsonRequest, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        super(url, jsonRequest, listener, errorListener);
    }

    public JsonArrayRequestWithSessionCheck(String url, JSONObject jsonRequest, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        super(url, jsonRequest, listener, errorListener);
    }


    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> map = new HashMap<>();
        map.put("Cookie", "JSESSIONID=" + ConfigUtils.getSessionId());
        return map;
    }

}
