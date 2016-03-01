package unicorn.com.xhsr;

import android.os.Bundle;
import android.os.Handler;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import unicorn.com.xhsr.base.BaseActivity;
import unicorn.com.xhsr.other.TinyDB;
import unicorn.com.xhsr.utils.ConfigUtils;
import unicorn.com.xhsr.utils.SfUtils;
import unicorn.com.xhsr.utils.ToastUtils;
import unicorn.com.xhsr.volley.SimpleVolley;
import unicorn.com.xhsr.volley.VolleyErrorHelper;


public class SplashActivity extends BaseActivity {

    String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        boolean rememberMe = TinyDB.getInstance().getBoolean(SfUtils.SF_REMEMBER_ME);
        if (rememberMe) {
            login();
        } else {
            delayToLoginActivity();
        }
    }

    private void login() {
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                ConfigUtils.getBaseUrl() + "/login",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        startActivityAndFinish(MainActivity.class);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ToastUtils.show(VolleyErrorHelper.getErrorMessage(error));
                        startActivityAndFinish(LoginActivity.class);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("username", TinyDB.getInstance().getString(SfUtils.SF_ACCOUNT));
                map.put("password", TinyDB.getInstance().getString(SfUtils.SF_PASSWORD));
                return map;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    String currentUserString = response.headers.get("currentUser");
                    JSONObject currentUser = new JSONObject(currentUserString);
                    role = currentUser.getString("role");
                    ConfigUtils.saveJSessionId(response);
                }
                catch (Exception e){
                    //
                }

                return super.parseNetworkResponse(response);
            }
        };
        SimpleVolley.addRequest(stringRequest);
    }

    private void delayToLoginActivity() {
        int delay = 1000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivityAndFinish(LoginActivity.class);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        }, delay);
    }

}
