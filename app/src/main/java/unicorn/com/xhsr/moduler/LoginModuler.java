package unicorn.com.xhsr.moduler;


import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import unicorn.com.xhsr.other.TinyDB;
import unicorn.com.xhsr.utils.ConfigUtils;
import unicorn.com.xhsr.utils.DialogUtils;
import unicorn.com.xhsr.utils.SfUtils;
import unicorn.com.xhsr.utils.ToastUtils;
import unicorn.com.xhsr.volley.SimpleVolley;
import unicorn.com.xhsr.volley.VolleyErrorHelper;

public class LoginModuler {

    String shiroLoginFailure = null;



    // show dialog?
    private void login(Context context, final String account, final String password) {
        final MaterialDialog mask = DialogUtils.showMask(context, "登录中", "请稍后");
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                ConfigUtils.getBaseUrl() + "/login",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mask.dismiss();
                        if (shiroLoginFailure != null) {
                            ToastUtils.show("账号或密码错误!");
                        }
                        else {
                        }
//                        boolean result = checkRole();
//                        if (result) {
//                            startActivityAndFinish(MainActivity.class);
//                        } else {
//                            ToastUtils.show("非法角色!");
//                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mask.dismiss();
                        ToastUtils.show(VolleyErrorHelper.getErrorMessage(error));
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("username", account);
                map.put("password", password);
                return map;
            }

            // 从返回的头部中获取一些信息
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                shiroLoginFailure = response.headers.get("shiroLoginFailure");
                // shiroLoginFailure != null 表示登录失败
                if (shiroLoginFailure != null) {
                    return super.parseNetworkResponse(response);
                }
                // 如果登录成功，保存 sessionId

                try {
                    // 获取用户信息
                    String currentUserString = response.headers.get("currentUser");
                    JSONObject currentUser = new JSONObject(currentUserString);
                    String personName = currentUser.getString("username");
                    String personCode = currentUser.getString("account");
                    TinyDB tinyDB = TinyDB.getInstance();
                    tinyDB.putString(SfUtils.SF_PERSON_NAME, personName);
                    tinyDB.putString(SfUtils.SF_PERSON_CODE, personCode);
                } catch (Exception e) {
//                    ToastUtils.show(e.getMessage());
                }

                ConfigUtils.saveJSessionId(response);
//                saveUserLoginInfo();
                return super.parseNetworkResponse(response);
            }
        };
        SimpleVolley.getRequestQueue().add(stringRequest);
    }


//    private void saveUserLoginInfo() {
//        TinyDB tinyDB = TinyDB.getInstance();
//        tinyDB.putString(SfUtils.SF_ACCOUNT, etAccount.getText().toString().trim());
//        tinyDB.putString(SfUtils.SF_PASSWORD, etPassword.getText().toString().trim());
//        tinyDB.putBoolean(SfUtils.SF_REMEMBER_ME, true);
//    }

}
