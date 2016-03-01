package unicorn.com.xhsr;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

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

import butterknife.Bind;
import butterknife.OnClick;
import unicorn.com.xhsr.base.BaseActivity;
import unicorn.com.xhsr.other.TinyDB;
import unicorn.com.xhsr.utils.ConfigUtils;
import unicorn.com.xhsr.utils.DialogUtils;
import unicorn.com.xhsr.utils.SfUtils;
import unicorn.com.xhsr.utils.ToastUtils;
import unicorn.com.xhsr.volley.SimpleVolley;
import unicorn.com.xhsr.volley.VolleyErrorHelper;


public class LoginActivity extends BaseActivity {


    // ================================== 全局变量 ==================================

    String role;

    String shiroLoginFailure;


    // ================================== views ==================================

    @Bind(R.id.et_account)
    EditText etAccount;

    @Bind(R.id.et_password)
    EditText etPassword;



    // ================================== onCreate ==================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
    }

    private void initViews() {
        boolean rememberMe = TinyDB.getInstance().getBoolean(SfUtils.SF_REMEMBER_ME);
        if (rememberMe) {
            String account = TinyDB.getInstance().getString(SfUtils.SF_ACCOUNT);
            etAccount.setText(account);
            String password = TinyDB.getInstance().getString(SfUtils.SF_PASSWORD);
            etPassword.setText(password);
        }
    }


    // ================================== login ==================================

    @OnClick(R.id.btn_login)
    public void loginOnClick() {
        if (checkInput()) {
            login();
        }
    }

    private boolean checkInput() {
        if (TextUtils.isEmpty(etAccount.getText())) {
            ToastUtils.show("账号不能为空");
            return false;
        }
        if (TextUtils.isEmpty(etPassword.getText())) {
            ToastUtils.show("密码不能为空");
            return false;
        }
        return true;
    }

    private void login() {
        final MaterialDialog mask = DialogUtils.showMask(this, "登录中", "请稍后");
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                ConfigUtils.getBaseUrl() + "/login",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mask.dismiss();
                        if (shiroLoginFailure != null) {
                            ToastUtils.show("账号或密码错误!");
                            return;
                        }
                        if (role != null) {
                            switch (role) {
                                case "Nurse":
                                case "Matron":
                                    startActivityAndFinish(MainActivity.class);
                                    break;
                                default:
                                    ToastUtils.show("非法角色!");
                            }
                        }
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
                map.put("username", etAccount.getText().toString().trim());
                map.put("password", etPassword.getText().toString().trim());
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
                // 如果登录成功，获取角色，保存 JSessionId
                try {
                    String currentUserString = response.headers.get("currentUser");

                    JSONObject currentUser = new JSONObject(currentUserString);
                    role = currentUser.getString("role");
                    ConfigUtils.saveJSessionId(response);
                } catch (Exception e) {
                    //
                }

                ConfigUtils.saveJSessionId(response);
                saveUserLoginInfo();
                return super.parseNetworkResponse(response);
            }
        };
        SimpleVolley.getRequestQueue().add(stringRequest);
    }

    private void saveUserLoginInfo() {
        TinyDB.getInstance().putString(SfUtils.SF_ACCOUNT, etAccount.getText().toString().trim());
        TinyDB.getInstance().putString(SfUtils.SF_PASSWORD, etPassword.getText().toString().trim());
        TinyDB.getInstance().putBoolean(SfUtils.SF_REMEMBER_ME, true);
    }

}
