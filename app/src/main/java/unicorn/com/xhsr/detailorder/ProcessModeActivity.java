package unicorn.com.xhsr.detailorder;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.f2prateek.dart.InjectExtra;

import org.simple.eventbus.Subscriber;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import unicorn.com.xhsr.R;
import unicorn.com.xhsr.data.DataHelp;
import unicorn.com.xhsr.other.ClickHelp;
import unicorn.com.xhsr.sheetSelect.BottomSheetActivity;
import unicorn.com.xhsr.sheetSelect.model.SelectObject;
import unicorn.com.xhsr.utils.ResultCodeUtils;


public class ProcessModeActivity extends BottomSheetActivity {


    // =============================== onCreate ===============================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_mode);
        showDefaultValue();
    }

    private void showDefaultValue() {
        tvProcessMode.setText(DataHelp.getValue(processModeDataList, processModeId));
        tvProcessTimeLimit.setText(DataHelp.getValue(processTimeLimitList, processTimeLimitId));
        tvEmergencyDegree.setText(DataHelp.getValue(emergencyDegreeDataList, emergencyDegreeId));
    }


    // =============================== 处理方式 ===============================

    @InjectExtra("processModeId")
    String processModeId;

    @Bind(R.id.tvProcessMode)
    TextView tvProcessMode;

    List<SelectObject> processModeDataList = DataHelp.getProcessModeDataList();

    @OnClick(R.id.processMode)
    public void processModeOnClick() {
        showSelectSheet("处理方式", processModeDataList, processModeId, "onProcessModeSelect");
    }

    @Subscriber(tag = "onProcessModeSelect")
    private void onProcessModeSelect(String idSelected) {
        processModeId = idSelected;
        tvProcessMode.setText(DataHelp.getValue(processModeDataList, idSelected));
        bottomSheet.dismissSheet();
    }


    // =============================== 处理时限 ===============================

    @InjectExtra("processTimeLimitId")
    String processTimeLimitId;

    List<SelectObject> processTimeLimitList = DataHelp.getProcessTimeLimitDataList();

    @Bind(R.id.tvProcessTimeLimit)
    TextView tvProcessTimeLimit;

    @OnClick(R.id.processTimeLimit)
    public void processTimeLimitOnClick() {
        showSelectSheet("处理时限", processTimeLimitList, processTimeLimitId, "onProcessTimeLimitSelect");
    }

    @Subscriber(tag = "onProcessTimeLimitSelect")
    private void onProcessTimeLimitSelect(String idSelected) {
        processTimeLimitId = idSelected;
        tvProcessTimeLimit.setText(DataHelp.getValue(processTimeLimitList, idSelected));
        bottomSheet.dismissSheet();
    }


    // =============================== 紧急程度 ===============================

    @InjectExtra("emergencyDegreeId")
    String emergencyDegreeId;

    List<SelectObject> emergencyDegreeDataList = DataHelp.getEmergencyDegreeDataList();

    @Bind(R.id.tvEmergencyDegree)
    TextView tvEmergencyDegree;

    @OnClick(R.id.emergencyDegree)
    public void emergencyDegreeOnClick() {
        showSelectSheet("紧急程度", emergencyDegreeDataList, emergencyDegreeId, "onEmergencyDegreeSelect");
    }

    @Subscriber(tag = "onEmergencyDegreeSelect")
    private void onEmergencyDegreeSelect(String idSelected) {
        emergencyDegreeId = idSelected;
        tvEmergencyDegree.setText(DataHelp.getValue(emergencyDegreeDataList, idSelected));
        bottomSheet.dismissSheet();
    }


    // =============================== 取消和确认 ===============================

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
        finishWithResult();
    }

    private void finishWithResult() {
        Intent data = new Intent();
        data.putExtra("processModeId", processModeId);
        data.putExtra("processTimeLimitId", processTimeLimitId);
        data.putExtra("emergencyDegreeId", emergencyDegreeId);
        setResult(ResultCodeUtils.PROCESS_MODE, data);
        finish();
    }


}
