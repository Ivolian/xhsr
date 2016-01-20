package unicorn.com.xhsr;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.f2prateek.dart.InjectExtra;

import org.simple.eventbus.Subscriber;

import butterknife.Bind;
import butterknife.OnClick;
import unicorn.com.xhsr.base.BottomSheetActivity;
import unicorn.com.xhsr.data.DataHelp;
import unicorn.com.xhsr.select.SelectAdapter;

public class ProcessModeActivity extends BottomSheetActivity {


    public static int PROCESS_MODE_RESULT_CODE = 1004;


    // =============================== onCreate ===============================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_mode);
        setDefaultValue();
    }

    private void setDefaultValue() {
        if (processModeId == null) {
            processModeId = dpProcessMode.getDataList().get(0).objectId;
        }
        onProcessModeSelect(processModeId);
        if (processTimeLimitId == null) {
            processTimeLimitId = dpProcessTimeLimit.getDataList().get(0).objectId;
        }
        onProcessTimeLimitSelect(processTimeLimitId);
        if (emergencyDegreeId == null) {
            emergencyDegreeId = dpEmergencyDegree.getDataList().get(0).objectId;
        }
        onEmergencyDegreeSelect(emergencyDegreeId);
    }


    // =============================== 处理方式 ===============================

    @InjectExtra("processModeId")
    @Nullable
    String processModeId;

    @Bind(R.id.tvProcessMode)
    TextView tvProcessMode;

    SelectAdapter.DataProvider dpProcessMode = DataHelp.getProcessModeDataProvider();

    @OnClick(R.id.processMode)
    public void processModeOnClick() {
        showSelectSheet("处理方式", dpProcessMode, processModeId, "onProcessModeSelect");
    }

    @Subscriber(tag = "onProcessModeSelect")
    private void onProcessModeSelect(String objectIdSelected) {
        processModeId = objectIdSelected;
        tvProcessMode.setText(DataHelp.getValue(dpProcessMode, objectIdSelected));
        bottomSheet.dismissSheet();
    }


    // =============================== 处理时限 ===============================

    @InjectExtra("processTimeLimitId")
    @Nullable
    String processTimeLimitId;

    SelectAdapter.DataProvider dpProcessTimeLimit = DataHelp.getProcessTimeLimitDataProvider();

    @Bind(R.id.tvProcessTimeLimit)
    TextView tvProcessTimeLimit;

    @OnClick(R.id.processTimeLimit)
    public void processTimeLimitOnClick() {
        showSelectSheet("处理时限", dpProcessTimeLimit, processTimeLimitId, "onProcessTimeLimitSelect");
    }

    @Subscriber(tag = "onProcessTimeLimitSelect")
    private void onProcessTimeLimitSelect(String objectIdSelected) {
        processTimeLimitId = objectIdSelected;
        tvProcessTimeLimit.setText(DataHelp.getValue(dpProcessTimeLimit, objectIdSelected));
        bottomSheet.dismissSheet();
    }


    // =============================== 紧急程度 ===============================

    @InjectExtra("emergencyDegreeId")
    @Nullable
    String emergencyDegreeId = null;

    SelectAdapter.DataProvider dpEmergencyDegree = DataHelp.getEmergencyDegreeDataProvider();

    @Bind(R.id.tvEmergencyDegree)
    TextView tvEmergencyDegree;

    @OnClick(R.id.emergencyDegree)
    public void emergencyDegreeOnClick() {
        showSelectSheet("紧急程度", dpEmergencyDegree, emergencyDegreeId, "onEmergencyDegreeSelect");
    }

    @Subscriber(tag = "onEmergencyDegreeSelect")
    private void onEmergencyDegreeSelect(String objectIdSelected) {
        emergencyDegreeId = objectIdSelected;
        tvEmergencyDegree.setText(DataHelp.getValue(dpEmergencyDegree, objectIdSelected));
        bottomSheet.dismissSheet();
    }


    // =============================== 基础方法 ===============================

    @OnClick(R.id.cancel)
    public void cancel() {
        finish();
    }

    @OnClick(R.id.confirm)
    public void confirm() {
        Intent data = new Intent();
        data.putExtra("processModeId", processModeId);
        data.putExtra("processTimeLimitId", processTimeLimitId);
        data.putExtra("emergencyDegreeId", emergencyDegreeId);
        setResult(PROCESS_MODE_RESULT_CODE, data);
        finish();
    }


}
