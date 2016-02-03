package unicorn.com.xhsr.satisfation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.joda.time.DateTime;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.OnClick;
import unicorn.com.xhsr.R;
import unicorn.com.xhsr.SimpleApplication;
import unicorn.com.xhsr.base.BaseActivity;
import unicorn.com.xhsr.data.DataHelp;
import unicorn.com.xhsr.data.greendao.Department;
import unicorn.com.xhsr.data.greendao.DepartmentDao;
import unicorn.com.xhsr.groupselect.GroupSelectActivity;
import unicorn.com.xhsr.groupselect.GroupSelectHelper;


public class PersonalInfoActivity extends BaseActivity {


    // ================================ onCreate ================================

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        initView();
    }

    private void initView() {
        initTvDate();
    }


    // ================================ 调查科室 ================================

    public static int DEPARTMENT_RESULT_CODE = 1001;

    @Bind(R.id.tvDepartment)
    TextView tvDepartment;

    @OnClick(R.id.department)
    public void departmentOnClick() {


        GroupSelectActivity.dataProvider = DataHelp.getDepartmentDataProvider();
        GroupSelectHelper.startGroupSelectActivity(this, "调查科室", DEPARTMENT_RESULT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == DEPARTMENT_RESULT_CODE) {
            String objectId = data.getStringExtra("objectId");
            Department department = SimpleApplication.getDaoSession().getDepartmentDao().queryBuilder().where(DepartmentDao.Properties.ObjectId.eq(objectId)).unique();
            tvDepartment.setText(department.getFullName());
        }
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
        datePickerDialog.show(getFragmentManager(), "SurveyDate");
    }

    private String getSurveyDateString() {
        return new DateTime(cSurveyDate).toString("yyyy - MM - dd");
    }


    // ================================ 开始调查 ================================

    @OnClick(R.id.start)
    public void startOnClick() {
        startActivity(SatisfactionActivity.class);
    }


    // ================================ cancel ================================

    @OnClick(R.id.cancel)
    public void cancelOnClick() {
        finish();
    }



}
