package unicorn.com.xhsr;

import android.content.Intent;
import android.os.Bundle;
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
import com.yo.libs.app.DimensCodeTools;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import unicorn.com.xhsr.base.BaseActivity;
import unicorn.com.xhsr.data.BasicDataGotter;
import unicorn.com.xhsr.goodwork.draglayout.view.DragLayout;
import unicorn.com.xhsr.myorder.MyOrderActivity;
import unicorn.com.xhsr.other.ClickHelp;
import unicorn.com.xhsr.other.DividerGridItemDecoration;
import unicorn.com.xhsr.satisfaction.BasicInfoActivity;
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
                ConfigUtils.getBaseUrl() + "/login",
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
                String sessionId =response.headers.get("jsessionid");
                ConfigUtils.setSessionId(sessionId);
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
        basicDataGotter.getOption();
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
        recyclerViewHeader.findViewById(R.id.satisfaction).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ClickHelp.isFastClick()) {
                    startActivity(BasicInfoActivity.class);
                }
            }
        });

        recyclerViewHeader.attachTo(recyclerView);
    }


    @Bind(R.id.tvToRepair)
    TextView tvToRepair;


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
    }

    @OnClick(R.id.toRepair)
    public void toRepairOnClick() {
        Intent intent = new Intent(this, MyOrderActivity.class);
        intent.putExtra("currentItem", 0);
        startActivity(intent);
    }

    @OnClick(R.id.repairing)
    public void repairingOnClick() {
        Intent intent = new Intent(this, MyOrderActivity.class);
        intent.putExtra("currentItem", 1);
        startActivity(intent);
    }

    @OnClick(R.id.repaired)
    public void repairedOnClick() {
        Intent intent = new Intent(this, MyOrderActivity.class);
        intent.putExtra("currentItem", 2);
        startActivity(intent);
    }


}
