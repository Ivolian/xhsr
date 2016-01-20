package unicorn.com.xhsr;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.flipboard.bottomsheet.BottomSheetLayout;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import unicorn.com.xhsr.greendao.EmergencyDegree;
import unicorn.com.xhsr.greendao.ProcessingMode;
import unicorn.com.xhsr.greendao.ProcessingTimeLimit;
import unicorn.com.xhsr.select.SelectAdapter;
import unicorn.com.xhsr.select.SelectObjectWithPosition;

public class ProcessModeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_process_mode);
        ActivityHelp.initActivity(this);
        initBottomSheet();
        setProcessModeDefaultValue();
        setProcessTimeLimitDefaultValue();
        setEmergencyDegreeDefaultValue();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


    @Bind(R.id.bottomsheet)
    BottomSheetLayout bottomSheet;

    @SuppressWarnings("deprecation")
    private void initBottomSheet() {
        WindowManager windowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        int height = windowManager.getDefaultDisplay().getHeight();
        bottomSheet.setPeekSheetTranslation(height * 0.65f);
    }


    private void showSelectSheet(String sheetTitle, String callbackTag, int positionSelected) {
        View rootView = LayoutInflater.from(this).inflate(R.layout.select_sheet, bottomSheet, false);
        TextView tvTitle = (TextView) rootView.findViewById(R.id.title);
        tvTitle.setText(sheetTitle);
        rootView.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheet.dismissSheet();
            }
        });
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new SelectAdapter(callbackTag, positionSelected));
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).build());
        recyclerView.scrollToPosition(positionSelected);
        bottomSheet.showWithSheetView(rootView);
    }

    //

    @Bind(R.id.tvProcessMode)
    TextView tvProcessMode;

    SelectObjectWithPosition sopProcessMode;

    @OnClick(R.id.processMode)
    public void selectHandleMode() {
        showSelectSheet("选择处理方式", "onProcessModeSelect", sopProcessMode == null ? -1 : sopProcessMode.position);
    }

    @Subscriber(tag = "onProcessModeSelect")
    private void onHandleModeSelect(SelectObjectWithPosition selectObjectProcessMode) {
        sopProcessMode = selectObjectProcessMode;
        bottomSheet.dismissSheet();
        tvProcessMode.setText(selectObjectProcessMode.value);
    }

    private void setProcessModeDefaultValue() {
        List<ProcessingMode> processingModeList = SimpleApplication.getDaoSession().getProcessingModeDao().loadAll();
        ProcessingMode processingMode = processingModeList.get(0);
        tvProcessMode.setText(processingMode.getName());
        sopProcessMode = new SelectObjectWithPosition();
        sopProcessMode.value = processingMode.getName();
        sopProcessMode.objectId = processingMode.getObjectId();
        sopProcessMode.position = 0;
    }

    //

    @Bind(R.id.tvProcessTimeLimit)
    TextView tvProcessTimeLimit;

    SelectObjectWithPosition sopProcessTimeLimit;

    @OnClick(R.id.processTimeLimit)
    public void timeLimitOnClick() {
        showSelectSheet("选择处理时限", "onProcessTimeLimitSelect", sopProcessTimeLimit == null ? -1 : sopProcessTimeLimit.position);
    }

    @Subscriber(tag = "onProcessTimeLimitSelect")
    private void onProcessTimeLimitSelect(SelectObjectWithPosition selectObjectProcessMode) {
        sopProcessTimeLimit = selectObjectProcessMode;
        bottomSheet.dismissSheet();
        tvProcessTimeLimit.setText(selectObjectProcessMode.value);
    }

    private void setProcessTimeLimitDefaultValue() {
        List<ProcessingTimeLimit> processingTimeLimitList = SimpleApplication.getDaoSession().getProcessingTimeLimitDao().loadAll();
        ProcessingTimeLimit processingTimeLimit = processingTimeLimitList.get(0);
        tvProcessTimeLimit.setText(processingTimeLimit.getName());
        sopProcessTimeLimit = new SelectObjectWithPosition();
        sopProcessTimeLimit.value = processingTimeLimit.getName();
        sopProcessTimeLimit.objectId = processingTimeLimit.getObjectId();
        sopProcessTimeLimit.position = 0;
    }

    //


    @Bind(R.id.tvEmergencyDegree)
    TextView tvEmergencyDegree;

    SelectObjectWithPosition sopEmergencyDegree;

    @OnClick(R.id.emergencyDegree)
    public void emergencyDegreeOnClick() {
        showSelectSheet("选择紧急程度", "onEmergencyDegreeSelect", sopEmergencyDegree == null ? -1 : sopEmergencyDegree.position);
    }

    @Subscriber(tag = "onEmergencyDegreeSelect")
    private void onEmergencyDegreeSelect(SelectObjectWithPosition selectObjectProcessMode) {
        sopEmergencyDegree = selectObjectProcessMode;
        bottomSheet.dismissSheet();
        tvEmergencyDegree.setText(selectObjectProcessMode.value);
    }

    private void setEmergencyDegreeDefaultValue() {
        List<EmergencyDegree> emergencyDegreeList = SimpleApplication.getDaoSession().getEmergencyDegreeDao().loadAll();
        EmergencyDegree emergencyDegree = emergencyDegreeList.get(0);
        tvEmergencyDegree.setText(emergencyDegree.getName());
        sopEmergencyDegree = new SelectObjectWithPosition();
        sopEmergencyDegree.value = emergencyDegree.getName();
        sopEmergencyDegree.objectId = emergencyDegree.getObjectId();
        sopEmergencyDegree.position = 0;
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
