package unicorn.com.xhsr;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.github.florent37.viewanimator.ViewAnimator;
import com.github.ppamorim.dragger.DraggerActivity;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import butterknife.Bind;
import butterknife.OnClick;
import unicorn.com.xhsr.groupselect.GroupSelectHelper;


public class QuickOrderActivity extends DraggerActivity {


    // =============================== onCreate & onDestroy ===============================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_quick_order);
        ActivityHelp.initActivity(this);
        initViews();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void initViews() {
        initEquipment();
        initBottomSheet();
    }


    // =============================== 选择需要维修的设备 ===============================

    @Bind(R.id.tdEquipment)
    ImageView tdEquipment;

    private void initEquipment() {
        int colorPrimary = ContextCompat.getColor(this, R.color.colorPrimary);
        TextDrawable textDrawable = TextDrawable.builder().buildRound("修", colorPrimary);
        tdEquipment.setImageDrawable(textDrawable);
    }

    @OnClick(R.id.equipment)
    public void equipmentOnClick() {
        GroupSelectHelper.startGroupSelectActivity(this, "设备", 3, EQUIPMENT_RESULT_CODE);
    }

    @Bind(R.id.tvEquipment)
    TextView tvEquipment;

    public int EQUIPMENT_RESULT_CODE = 1;

    public int ADDRESS_RESULT_CODE = 2;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == EQUIPMENT_RESULT_CODE) {
            String result = data.getStringExtra(GroupSelectHelper.RESULT);
            tvEquipment.setText(result);
        }
        if (resultCode == ADDRESS_RESULT_CODE) {
            String result = data.getStringExtra(GroupSelectHelper.RESULT);
            tvAddress.setText(result);
        }
    }

    @Bind(R.id.tvAddress)
    TextView tvAddress;

    @OnClick(R.id.address)
    public void addressOnClick() {
        GroupSelectHelper.startGroupSelectActivity(this, "维修地址", 1, ADDRESS_RESULT_CODE);
    }

    // =============================== bottom sheet ===============================

    @Bind(R.id.bottomsheet)
    BottomSheetLayout bottomSheet;

    @SuppressWarnings("deprecation")
    private void initBottomSheet() {
        WindowManager windowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        int height = windowManager.getDefaultDisplay().getHeight();
        bottomSheet.setPeekSheetTranslation(height * 0.65f);
    }


    // =============================== 设备故障 ===============================

    @Bind(R.id.tvBreakdown)
    TextView tvBreakdown;

    SelectObject soBreakdown;

    @OnClick(R.id.breakdown)
    public void selectBreakdown() {
        showSelectSheet("选择设备故障", "onBreakdownSelect", soBreakdown == null ? -1 : soBreakdown.position);
    }

    @Subscriber(tag = "onBreakdownSelect")
    private void onBreakdownSelect(SelectObject selectObject) {
        soBreakdown = selectObject;
        bottomSheet.dismissSheet();
        String breakdown = (String) selectObject.value;
        tvBreakdown.setText(breakdown);
    }


    // =============================== 处理方式 ===============================

    @Bind(R.id.tvHandleMode)
    TextView tvHandleMode;

    SelectObject soHandleMode;

    @OnClick(R.id.handleMode)
    public void selectHandleMode() {
        showSelectSheet("选择处理方式", "onHandleModeSelect", soHandleMode == null ? -1 : soHandleMode.position);
    }

    @Subscriber(tag = "onHandleModeSelect")
    private void onHandleModeSelect(SelectObject selectObject) {
        soHandleMode = selectObject;
        bottomSheet.dismissSheet();
        String handleMode = (String) selectObject.value;
        tvHandleMode.setText(handleMode);
    }


    // =============================== 补充说明 ===============================

    @Bind(R.id.descriptionArrow)
    ImageView ivDescriptionArrow;

    @Bind(R.id.elDescription)
    ExpandableRelativeLayout elDescription;

    @OnClick(R.id.description)
    public void descriptionOnClick() {
        ViewAnimator
                .animate(ivDescriptionArrow)
                .rotation(elDescription.isExpanded() ? 0 : 90)
                .duration(300)
                .start();
        elDescription.toggle();
    }


    // =============================== showSelectSheet ===============================

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


    // =============================== 基础方法 ===============================

    @OnClick(R.id.cancel)
    public void cancel() {
        closeActivity();
    }


}
