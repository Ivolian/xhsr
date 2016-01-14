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
import com.github.ppamorim.dragger.DraggerActivity;
import com.github.ppamorim.dragger.DraggerPosition;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import unicorn.com.xhsr.groupselect.GroupSelectActivity;


public class QuickOrderActivity extends DraggerActivity {


    // =============================== onCreate & onDestroy ===============================

    @OnClick(R.id.cancel)
    public void cancel(){
        closeActivity();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 2333) {
            String result = data.getStringExtra("result");
            tvRepair.setText(result);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_quick_order);
        ButterKnife.bind(this);
        initViews();

//        setShadowView(R.drawable.shadow);

        setSlideEnabled(false);
      setTension(1);
        setDraggerPosition(DraggerPosition.RIGHT);


    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void initViews() {
        initRepairTextDrawable();
        initBottomSheet();
    }

    @Bind(R.id.tvRepair)
    TextView tvRepair;

    @OnClick(R.id.repair)
    public void test() {
        Intent intent = new Intent(this, GroupSelectActivity.class);
        intent.putExtra("title","选择设备");
        intent.putExtra("maxLevel",5);
        startActivityForResult(intent, 2333);
    }



    // =============================== repair text drawable ===============================

    @Bind(R.id.repair_text_drawable)
    ImageView ivRepairTextDrawable;

    private void initRepairTextDrawable() {
        int colorPrimary = ContextCompat.getColor(this, R.color.colorPrimary);
        TextDrawable textDrawable = TextDrawable.builder().buildRound("修", colorPrimary);
        ivRepairTextDrawable.setImageDrawable(textDrawable);
    }

    // =============================== bottom sheet ===============================

    @Bind(R.id.bottomsheet)
    BottomSheetLayout bottomSheet;

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
        showSelectSheetView("选择设备故障", "onBreakdownSelect", soBreakdown);
    }

    @Subscriber(tag = "onBreakdownSelect")
    private void onBreakdownSelect(SelectObject selectObject) {
        soBreakdown = selectObject;
        tvBreakdown.setText(selectObject.value);
        bottomSheet.dismissSheet();
    }

    // =============================== 处理方式 ===============================

    @Bind(R.id.tvHandleMode)
    TextView tvHandleMode;

    SelectObject soHandleMode;

    @OnClick(R.id.handleMode)
    public void selectHandleMode() {
        showSelectSheetView("选择处理方式", "onHandleModeSelect", soHandleMode);
    }

    @Subscriber(tag = "onHandleModeSelect")
    private void onHandleModeSelect(SelectObject selectObject) {
        soHandleMode = selectObject;
        tvHandleMode.setText(selectObject.value);
        bottomSheet.dismissSheet();
    }


    private void showSelectSheetView(String title, String eventTag, SelectObject selectObject) {
        View rootView = LayoutInflater.from(this).inflate(R.layout.fragment_select, bottomSheet, false);
        rootView.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheet.dismissSheet();
            }
        });
        TextView tvTitle = (TextView) rootView.findViewById(R.id.title);
        tvTitle.setText(title);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new SelectAdapter(eventTag, selectObject));
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).build());
        if (selectObject != null) {
            recyclerView.scrollToPosition(selectObject.position);
        }
        bottomSheet.showWithSheetView(rootView);
    }


}
