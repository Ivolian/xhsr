package unicorn.com.xhsr;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import butterknife.Bind;
import butterknife.OnClick;
import unicorn.com.xhsr.base.BaseActivity;
import unicorn.com.xhsr.moduler.LoginModuler;
import unicorn.com.xhsr.other.ClickHelp;
import unicorn.com.xhsr.other.TinyDB;
import unicorn.com.xhsr.utils.SfUtils;
import unicorn.com.xhsr.utils.ToastUtils;


public class LoginActivity extends BaseActivity {


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
        // 如果已经登录过了
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
    public void loginBtnOnClick() {
        if (ClickHelp.isFastClick()) {
            return;
        }
        if (TextUtils.isEmpty(etAccount.getText())) {
            ToastUtils.show("账号不能为空");
            return;
        }
        if (TextUtils.isEmpty(etPassword.getText())) {
            ToastUtils.show("密码不能为空");
            return;
        }
        LoginModuler loginModuler = new LoginModuler(etAccount.getText().toString().trim(), etPassword.getText().toString().trim());
        loginModuler.login(this, true);
    }


}
