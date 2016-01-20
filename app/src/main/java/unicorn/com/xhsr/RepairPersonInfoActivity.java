package unicorn.com.xhsr;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.OnClick;
import unicorn.com.xhsr.base.BaseActivity;
import unicorn.com.xhsr.data.DataHelp;
import unicorn.com.xhsr.groupselect.GroupSelectActivity;
import unicorn.com.xhsr.groupselect.GroupSelectHelper;
import unicorn.com.xhsr.select.SelectObject;
import unicorn.com.xhsr.utils.ToastUtils;


public class RepairPersonInfoActivity extends BaseActivity {


    public static int REPAIR_PERSON_INFO_RESULT_CODE = 1005;


    // =============================== onCreate ===============================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_person);
    }


    // =============================== views ===============================

    @Bind(R.id.personName)
    EditText etPersonName;

    @Bind(R.id.personCode)
    EditText etPersonCode;


    // =============================== 报修部门 ===============================

    String departmentId = null;

    @Bind(R.id.tvDepartment)
    TextView tvDepartment;

    @OnClick(R.id.department)
    public void departmentOnClick() {
        GroupSelectActivity.dataProvider = DataHelp.getDepartmentDataProvider();
        GroupSelectHelper.startGroupSelectActivity(this, "报修部门", 1, 2333);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SelectObject selectObject = (SelectObject) data.getSerializableExtra(GroupSelectHelper.RESULT);
        tvDepartment.setText(selectObject.value);
    }


    // =============================== 基础方法 ===============================

    @OnClick(R.id.cancel)
    public void cancel() {
        finish();
    }

    @OnClick(R.id.confirm)
    public void confirm() {
        if (TextUtils.isEmpty(etPersonName.getText())) {
            ToastUtils.show("报修人员不能为空！");
            return;
        }
        Intent data = new Intent();
        data.putExtra("personName", etPersonName.getText().toString());
        setResult(REPAIR_PERSON_INFO_RESULT_CODE, data);
        finish();
    }


}
