package unicorn.com.xhsr.quickorder;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.andreabaccega.widget.FormEditText;
import com.f2prateek.dart.InjectExtra;

import butterknife.Bind;
import butterknife.OnClick;
import unicorn.com.xhsr.R;
import unicorn.com.xhsr.SimpleApplication;
import unicorn.com.xhsr.base.BaseActivity;
import unicorn.com.xhsr.data.DataHelp;
import unicorn.com.xhsr.data.greendao.Department;
import unicorn.com.xhsr.data.greendao.DepartmentDao;
import unicorn.com.xhsr.groupselect.GroupSelectHelper;
import unicorn.com.xhsr.other.ClickHelp;
import unicorn.com.xhsr.utils.ResultCodeUtils;
import unicorn.com.xhsr.utils.ToastUtils;


public class RepairPersonActivity extends BaseActivity {


    // =============================== extra ===============================
    // 报修人员，人员工号，报修部门

    @Nullable
    @InjectExtra("personName")
    String personName;

    @Nullable
    @InjectExtra("personCode")
    String personCode;

    @Nullable
    @InjectExtra("departmentId")
    String departmentId;


    // =============================== extra ===============================

    @Bind(R.id.etPersonName)
    FormEditText etPersonName;

    @Bind(R.id.etPersonCode)
    FormEditText etPersonCode;

    @Bind(R.id.tvDepartment)
    TextView tvDepartment;


    // =============================== onCreate ===============================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_person);
        initViews();
    }

    private void initViews() {
        etPersonName.setText(personName != null ? personName : "");
        etPersonCode.setText(personCode != null ? personCode : "");
        notifyDepartmentChange();
    }

    private void notifyDepartmentChange() {
        if (departmentId != null) {
            Department department = SimpleApplication.getDaoSession().getDepartmentDao().queryBuilder().where(DepartmentDao.Properties.ObjectId.eq(departmentId)).unique();
            tvDepartment.setText(department.getFullName());
        }
    }


    // =============================== 报修部门 ===============================

    @OnClick(R.id.department)
    public void departmentOnClick() {
        if (ClickHelp.isFastClick()) {
            return;
        }
        GroupSelectHelper.startGroupSelectActivity(this, DataHelp.getDepartmentDataProvider(), "报修部门", departmentId, ResultCodeUtils.DEPARTMENT);
    }


    // =============================== onActivityResult ===============================

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ResultCodeUtils.DEPARTMENT) {
            departmentId = data.getStringExtra("subId");
            notifyDepartmentChange();
        }
    }


    // =============================== 基础方法 ===============================

    @OnClick(R.id.cancel)
    public void cancel() {
        if (ClickHelp.isFastClick()) {
            return;
        }
        finish();
    }

    @OnClick(R.id.confirm)
    public void confirm() {
        if (ClickHelp.isFastClick()) {
            return;
        }
        if (!etPersonName.testValidity()) {
            return;
        }
        if (!etPersonCode.testValidity()) {
            return;
        }
        if (departmentId == null) {
            ToastUtils.show("请选择报修部门");
            return;
        }
        finishWithResult();
    }

    private void finishWithResult(){
        Intent data = new Intent();
        data.putExtra("personName", etPersonName.getText().toString());
        data.putExtra("personCode", etPersonCode.getText().toString());
        data.putExtra("departmentId", departmentId);
        setResult(ResultCodeUtils.REPAIR_PERSON, data);
        finish();
    }

}
