package unicorn.com.xhsr.satisfaction;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.TextView;

import com.andreabaccega.widget.FormEditText;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.joda.time.DateTime;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.OnClick;
import unicorn.com.xhsr.R;
import unicorn.com.xhsr.utils.ResultCodeUtils;
import unicorn.com.xhsr.SimpleApplication;
import unicorn.com.xhsr.base.BaseActivity;
import unicorn.com.xhsr.data.DataHelp;
import unicorn.com.xhsr.data.greendao.Department;
import unicorn.com.xhsr.data.greendao.DepartmentDao;
import unicorn.com.xhsr.groupselect.GroupSelectActivity;
import unicorn.com.xhsr.groupselect.GroupSelectHelper;
import unicorn.com.xhsr.other.ClickHelp;
import unicorn.com.xhsr.satisfaction.model.SatisfactionResult;
import unicorn.com.xhsr.utils.ToastUtils;


public class BasicInfoActivity extends BaseActivity {


    // ================================ onCreate ================================

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_info);
        initView();
    }

    private void initView() {
        initTvDate();
    }


    // ================================ 调查科室 ================================

    String departmentId;

    @Bind(R.id.tvDepartment)
    TextView tvDepartment;

    @OnClick({R.id.department})
    public void departmentOnClick() {
        if (ClickHelp.isFastClick()) {
            return;
        }
        GroupSelectHelper.startGroupSelectActivity(this, DataHelp.getDepartmentDataProvider(),"调查科室",departmentId, ResultCodeUtils.DEPARTMENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ResultCodeUtils.DEPARTMENT) {
            departmentId = data.getStringExtra("objectId");
            Department department = SimpleApplication.getDaoSession().getDepartmentDao().queryBuilder()
                    .where(DepartmentDao.Properties.ObjectId.eq(departmentId))
                    .unique();
            tvDepartment.setText(department.getFullName());
        }
    }


    // ================================ 受调查人姓名和电话 ================================

    @Bind(R.id.etName)
    FormEditText etName;

    @Bind(R.id.etPhone)
    FormEditText etPhone;

    private boolean checkInput() {
        if (TextUtils.isEmpty(tvDepartment.getText())) {
            ToastUtils.show("请选选择调查科室");
            return false;
        }
        return etName.testValidity() && etPhone.testValidity();
    }


    // ================================ 调查日期 ================================

    Calendar cSurveyDate;

    @Bind(R.id.tvSurveyDate)
    TextView tvSurveyDate;

    private void initTvDate() {
        Calendar c25 = Calendar.getInstance();
        c25.set(Calendar.DAY_OF_MONTH, 25);
        cSurveyDate = c25;
        tvSurveyDate.setText(getSurveyDateString());
    }

    @OnClick(R.id.surveyDate)
    public void surveyDateOnClick() {
        if (ClickHelp.isFastClick()) {
            return;
        }
        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        cSurveyDate.set(Calendar.YEAR, year);
                        cSurveyDate.set(Calendar.MONTH, monthOfYear);
                        cSurveyDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        tvSurveyDate.setText(getSurveyDateString());
                    }
                },
                cSurveyDate.get(Calendar.YEAR),
                cSurveyDate.get(Calendar.MONTH),
                cSurveyDate.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.setTitle("调查日期");
        Calendar c25 = Calendar.getInstance();
        c25.set(Calendar.DAY_OF_MONTH, 25);
        datePickerDialog.setMinDate(c25);
        Calendar c28 = Calendar.getInstance();
        c28.set(Calendar.DAY_OF_MONTH, 28);
        datePickerDialog.setMaxDate(c28);
        datePickerDialog.show(getFragmentManager(), "surveyDate");
    }

    private String getSurveyDateString() {
        return new DateTime(cSurveyDate).toString("yyyy - MM - dd");
    }


    // ================================ 开始调查 ================================

    @OnClick(R.id.start)
    public void startOnClick() {
        if (ClickHelp.isFastClick()) {
            return;
        }
        if (checkInput()) {
            Intent intent = new Intent(this, SatisfactionActivity.class);
            intent.putExtra("satisfactionResult", generateSatisfactionResult());
            startActivity(intent);
            finish();
        }
    }

    private SatisfactionResult generateSatisfactionResult() {
        SatisfactionResult satisfactionResult = new SatisfactionResult();
        satisfactionResult.setDepartmentId(departmentId);
        satisfactionResult.setUsername(etName.getText().toString());
        satisfactionResult.setAssessDate(cSurveyDate.getTime().getTime());
        satisfactionResult.setPhone(etPhone.getText().toString());
        return satisfactionResult;
    }


    // ================================ cancel ================================

    @OnClick(R.id.cancel)
    public void cancelOnClick() {
        if (ClickHelp.isFastClick()) {
            return;
        }
        finish();
    }


}
