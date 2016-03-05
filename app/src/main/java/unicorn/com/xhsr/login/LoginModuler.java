package unicorn.com.xhsr.login;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.HashMap;
import java.util.Map;

import unicorn.com.xhsr.MainActivity;
import unicorn.com.xhsr.login.model.CurrentUser;
import unicorn.com.xhsr.other.TinyDB;
import unicorn.com.xhsr.utils.ConfigUtils;
import unicorn.com.xhsr.utils.SharedPreferencesUtils;
import unicorn.com.xhsr.utils.ToastUtils;
import unicorn.com.xhsr.volley.SimpleVolley;
import unicorn.com.xhsr.volley.VolleyErrorHelper;

public class LoginModuler {


    // ================================ constructor ================================

    String shiroLoginFailure;
    CurrentUser currentUser;
    String account;
    String password;

    public LoginModuler(String account, String password) {
        this.account = account;
        this.password = password;
    }

    // ================================ mask ================================

    private KProgressHUD kProgressHUD;

    private void showMask(Context context) {
        kProgressHUD = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false)
                .setAnimationSpeed(1)
                .setDimAmount(0.5f)
                .show();
    }

    private void hideMask() {
        if (kProgressHUD != null) {
            kProgressHUD.dismiss();
        }
    }


    // ================================ login ================================

    public void login(final Activity activity, boolean shouldShowMask) {
        if (shouldShowMask) {
            showMask(activity);
        }
        String url = ConfigUtils.getBaseUrl() + "/login";
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        // 除了登录以外的请求，都不用 String
                        // 这样出现解析错误就重新登录
                        // response 是一段 html
                        hideMask();
                        if (shiroLoginFailure != null) {
                            ToastUtils.show("账号或密码错误!");
                            return;
                        }
                        String role = currentUser.getRole();
                        if (!role.equals("Nurse") && !role.equals("Matron")) {
                            ToastUtils.show("非法角色!");
                            return;
                        }
                        Intent intent = new Intent(activity, MainActivity.class);
                        activity.startActivity(intent);
                        activity.finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideMask();
                        ToastUtils.show(VolleyErrorHelper.getErrorMessage(error));
                    }
                }) {
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

                // shiroLoginFailure != null 表示登录失败
                shiroLoginFailure = response.headers.get("shiroLoginFailure");
                if (shiroLoginFailure != null) {
                    return super.parseNetworkResponse(response);
                }

                // 如果登录成功，保存 sessionId 以及其他用户信息
                ConfigUtils.saveSessionId(response);
                String currentUserString = response.headers.get("currentUser");
                currentUser = new Gson().fromJson(currentUserString, CurrentUser.class);
                TinyDB tinyDB = TinyDB.getInstance();
                tinyDB.putString(SharedPreferencesUtils.ACCOUNT, account);
                tinyDB.putString(SharedPreferencesUtils.PASSWORD, password);
                tinyDB.putString(SharedPreferencesUtils.USERNAME, currentUser.getUsername());
                tinyDB.putBoolean(SharedPreferencesUtils.HAS_LOGIN, true);
                return super.parseNetworkResponse(response);
            }
        };
        SimpleVolley.addRequest(stringRequest);
    }


}
