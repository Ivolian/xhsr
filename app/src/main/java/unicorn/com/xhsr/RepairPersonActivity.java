package unicorn.com.xhsr;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.f2prateek.dart.InjectExtra;

import butterknife.Bind;
import butterknife.OnClick;
import unicorn.com.xhsr.base.BaseActivity;
import unicorn.com.xhsr.data.DataHelp;
import unicorn.com.xhsr.data.greendao.Department;
import unicorn.com.xhsr.data.greendao.DepartmentDao;
import unicorn.com.xhsr.groupselect.GroupSelectActivity;
import unicorn.com.xhsr.groupselect.GroupSelectHelper;
import unicorn.com.xhsr.utils.ToastUtils;


public class RepairPersonActivity extends BaseActivity {


    // =============================== onCreate ===============================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_person);
        if (personName != null) {
            etPersonName.setText(personName);
        }
    }


    // =============================== views ===============================

    @Nullable
    @InjectExtra("personName")
    String personName;

    @Bind(R.id.personName)
    EditText etPersonName;

    @Bind(R.id.personCode)
    EditText etPersonCode;


    // =============================== 报修部门 ===============================

    public static int DEPARTMENT_RESULT_CODE = 1001;

    String departmentId;

    @Bind(R.id.tvDepartment)
    TextView tvDepartment;

    @OnClick(R.id.department)
    public void departmentOnClick() {
        GroupSelectActivity.dataProvider = DataHelp.getDepartmentDataProvider();
        GroupSelectHelper.startGroupSelectActivity(this, "报修部门", DEPARTMENT_RESULT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == DEPARTMENT_RESULT_CODE) {
            departmentId = data.getStringExtra("objectId");
            Department department = SimpleApplication.getDaoSession().getDepartmentDao().queryBuilder().where(DepartmentDao.Properties.ObjectId.eq(departmentId)).unique();
            tvDepartment.setText(department.getFullName());
        }
    }


    // =============================== 基础方法 ===============================

    @OnClick(R.id.cancel)
    public void cancel() {
        finish();
    }

    @OnClick(R.id.confirm)
    public void confirm() {
        if (TextUtils.isEmpty(etPersonName.getText())) {
            ToastUtils.show("报修人员不能为空");
            return;
        }
        if (TextUtils.isEmpty(etPersonCode.getText())) {
            ToastUtils.show("人员工号不能为空");
            return;
        }
        if (departmentId == null) {
            ToastUtils.show("请选择报修部门");
            return;
        }

        Intent data = new Intent();
        data.putExtra("personName", etPersonName.getText().toString());
        data.putExtra("personCode", etPersonCode.getText().toString());
        data.putExtra("departmentId", departmentId);

        setResult(QuickOrderActivity.REPAIR_PERSON_RESULT_CODE, data);
        finish();
    }


}
