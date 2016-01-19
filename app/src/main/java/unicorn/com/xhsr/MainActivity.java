package unicorn.com.xhsr;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.bartoszlipinski.recyclerviewheader.RecyclerViewHeader;
import com.yo.libs.app.DimensCodeTools;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import unicorn.com.xhsr.draglayout.view.DragLayout;
import unicorn.com.xhsr.other.DividerGridItemDecoration;

public class MainActivity extends AppCompatActivity {


    // =============================== onCreate ===============================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initViews();
    }

    @Bind(R.id.waitRepair)
    TextView tvWaitRepair;

    private void initViews() {
        initDragLayout();
        initRecyclerView();

        BasicDataGotter basicDataGotter = new BasicDataGotter();
        basicDataGotter.getProcessMode();
        basicDataGotter.getProcessTimeLimit();
        basicDataGotter.getEmergencyDegree();
        basicDataGotter.getEquipment();
        basicDataGotter.getBuildingAndFloor();
        basicDataGotter.getDepartment();


        }


    // =============================== drag layout ===============================

    @Bind(R.id.dl)
    DragLayout dragLayout;

    private void initDragLayout() {
        dragLayout.setDragListener(new DragLayout.DragListener() {
            @Override
            public void onOpen() {
            }

            @Override
            public void onClose() {
            }

            @Override
            public void onDrag(float percent) {
            }
        });
    }


    // =============================== recycleview ===============================

    @Bind(R.id.recycleView)
    RecyclerView recyclerView;

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.addItemDecoration(new DividerGridItemDecoration(this));
        recyclerView.setAdapter(new MainAdapter());
        initRecycleViewHeader();
    }


    long lastClickTime = 0;

    private void initRecycleViewHeader() {
        RecyclerViewHeader recyclerViewHeader = RecyclerViewHeader.fromXml(this, R.layout.recycle_view_head);
        recyclerViewHeader.findViewById(R.id.quickOrder).setBackground(getCircleDrawable(R.color.md_blue_400));
        recyclerViewHeader.findViewById(R.id.test2).setBackground(getCircleDrawable(R.color.md_teal_400));
        recyclerViewHeader.findViewById(R.id.test3).setBackground(getCircleDrawable(R.color.md_red_400));


        recyclerViewHeader.findViewById(R.id.quick_order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastClickTime != 0) {
                    if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
                        return;
                    }
                }
                lastClickTime = SystemClock.elapsedRealtime();
                Intent intent = new Intent(MainActivity.this, QuickOrderActivity.class);
                startActivityForResult(intent,2333);

            }
        });

        recyclerViewHeader.attachTo(recyclerView);
    }

    private TextDrawable getCircleDrawable(@ColorRes int colorRes) {
        int color = ContextCompat.getColor(this, colorRes);
        return TextDrawable.builder().buildRound("", color);

    }


    // =============================== onClick ===============================


    public static int  SCAN_RESULT_CODE = 1001;

    @OnClick(R.id.scan)
    public void scanOnClick(){
        DimensCodeTools.startScan(this);
    }

@Bind(R.id.equipmentCode)
    EditText etEquipmentCode;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String res = DimensCodeTools.scanForResult(requestCode, resultCode, data);
       if (res!=null)
            etEquipmentCode.setText(res);

        tvWaitRepair.setText(DataHelp.wait_repair?"待维修(1)":"待维修");
    }
}
