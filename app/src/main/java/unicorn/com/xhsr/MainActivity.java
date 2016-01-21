package unicorn.com.xhsr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.bartoszlipinski.recyclerviewheader.RecyclerViewHeader;
import com.flyco.animation.BounceEnter.BounceTopEnter;
import com.flyco.animation.SlideExit.SlideBottomExit;
import com.flyco.dialog.widget.NormalDialog;
import com.yo.libs.app.DimensCodeTools;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import unicorn.com.xhsr.base.BaseActivity;
import unicorn.com.xhsr.data.BasicDataGotter;
import unicorn.com.xhsr.data.DataHelp;
import unicorn.com.xhsr.draglayout.view.DragLayout;
import unicorn.com.xhsr.other.ClickHelp;
import unicorn.com.xhsr.other.DividerGridItemDecoration;
import unicorn.com.xhsr.utils.ConfigUtils;
import unicorn.com.xhsr.utils.TextDrawableUtils;
import unicorn.com.xhsr.volley.SimpleVolley;

public class MainActivity extends BaseActivity {


    // =============================== onCreate ===============================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        initViews();
        getSessionId();


    }

    private void initViews() {
        initDragLayout();
        initRecyclerView();
    }

    private void getSessionId() {
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                "http://withub.net.cn/hems/login",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    }
                },
                SimpleVolley.getDefaultErrorListener()
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("username", "admin");
                map.put("password", "admin");
                return map;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                ConfigUtils.SESSION_ID = response.headers.get("jsessionid");
                EventBus.getDefault().post(new Object(), "getBasicData");
                return super.parseNetworkResponse(response);
            }
        };
        SimpleVolley.getRequestQueue().add(stringRequest);
    }

    @Subscriber(tag = "getBasicData")
    public void getBasicData(Object o) {
        BasicDataGotter basicDataGotter = new BasicDataGotter();
        basicDataGotter.getProcessMode();
        basicDataGotter.getProcessTimeLimit();
        basicDataGotter.getEmergencyDegree();
        basicDataGotter.getEquipment();
        basicDataGotter.getBuilding();
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

    private void initRecycleViewHeader() {
        RecyclerViewHeader recyclerViewHeader = RecyclerViewHeader.fromXml(this, R.layout.recycle_view_head);
        recyclerViewHeader.findViewById(R.id.message).setBackground(TextDrawableUtils.getCircleDrawable(this, R.color.md_blue_400));
        recyclerViewHeader.findViewById(R.id.takePhoto).setBackground(TextDrawableUtils.getCircleDrawable(this, R.color.md_teal_400));
        recyclerViewHeader.findViewById(R.id.video).setBackground(TextDrawableUtils.getCircleDrawable(this, R.color.md_red_400));
        recyclerViewHeader.findViewById(R.id.quick_order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ClickHelp.isFastClick()) {
                    Intent intent = new Intent(MainActivity.this, QuickOrderActivity.class);
                    startActivityForResult(intent, 2333);
                }
            }
        });
        recyclerViewHeader.attachTo(recyclerView);
    }


    @Bind(R.id.tvWaitRepair)
    TextView tvWaitRepair;


    // =============================== onClick ===============================


    @OnClick(R.id.scan)
    public void scanOnClick() {
        DimensCodeTools.startScan(this);
    }

    @Bind(R.id.equipmentCode)
    EditText etEquipmentCode;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String res = DimensCodeTools.scanForResult(requestCode, resultCode, data);
        if (res != null)
            etEquipmentCode.setText(res);

        tvWaitRepair.setText(DataHelp.wait_repair ? "待维修(1)" : "待维修");
    }

    @OnClick(R.id.waitRepair)
    public void waitRepairOnClick() {
        Intent intent = new Intent(this, WaitRepairActivity.class);
        startActivity(intent);
    }
}
