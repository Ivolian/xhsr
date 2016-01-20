package unicorn.com.xhsr;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.OnClick;
import unicorn.com.xhsr.base.BaseActivity;
import unicorn.com.xhsr.data.DataHelp;
import unicorn.com.xhsr.groupselect.GroupSelectActivity;
import unicorn.com.xhsr.groupselect.GroupSelectHelper;
import unicorn.com.xhsr.select.SelectObject;


public class RepairPersonActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_person);

    }

    @OnClick(R.id.department)
    public void departmentOnClick() {
        GroupSelectActivity.dataProvider = DataHelp.getDepartmentDataProvider();
        GroupSelectHelper.startGroupSelectActivity(this, "报修部门", 1, 2333);
    }


    @Bind(R.id.tvDepartment)
    TextView tvDepartment;

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
        finish();
    }




}
