package unicorn.com.xhsr.volley;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import unicorn.com.xhsr.utils.ToastUtils;


public class SimpleVolley {

    private static RequestQueue mRequestQueue;

    private SimpleVolley() {
        // no instances
    }

    static public void init(Context context) {
        mRequestQueue = Volley.newRequestQueue(context);
    }

    public static void addRequest(Request request) {
        request.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        getRequestQueue().add(request);
    }

    public static RequestQueue getRequestQueue() {
        if (mRequestQueue != null) {
            return mRequestQueue;
        } else {
            throw new IllegalStateException("RequestQueue not initialized");
        }
    }

    public static Response.ErrorListener getDefaultErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                try {
                    String responseJsonString = new String(volleyError.networkResponse.data, "UTF-8");
                    JSONObject response = new JSONObject(responseJsonString);
                    String errorMsg = response.getString("error");
                    ToastUtils.show(errorMsg);
                } catch (Exception e) {
                    ToastUtils.show(VolleyErrorHelper.getErrorMessage(volleyError));
                }
            }
        };
    }

}