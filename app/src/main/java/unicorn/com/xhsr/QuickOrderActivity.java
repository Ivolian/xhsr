package unicorn.com.xhsr;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class QuickOrderActivity extends AppCompatActivity {


    // =============================== onCreate & onDestroy ===============================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_quick_order);
        ButterKnife.bind(this);

        initRepairTextDrawable();
        SlidrConfig config = new SlidrConfig.Builder().edge(true).build();
        Slidr.attach(this, config);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


    // =============================== repair text drawable ===============================

    @Bind(R.id.repair_text_drawable)
    ImageView ivRepairTextDrawable;

    private void initRepairTextDrawable() {
        int colorPrimary = ContextCompat.getColor(this,R.color.colorPrimary);
        TextDrawable textDrawable = TextDrawable.builder().buildRound("修", colorPrimary);
        ivRepairTextDrawable.setImageDrawable(textDrawable);
    }


    // =============================== 设备故障 ===============================

    @Bind(R.id.tvBreakdown)
    TextView tvBreakdown;

    @OnClick(R.id.breakdown)
    public void selectBreakdown() {

        View rootView = LayoutInflater.from(this).inflate(R.layout.fragment_select, bottomSheetLayout, false);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new SelectAdapter("onBreakdownSelect",soBreakdown));
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).build());
        bottomSheetLayout.showWithSheetView(rootView);
        if (soBreakdown!=null){
            recyclerView.scrollToPosition(soBreakdown.position);

        }


//        SelectFragment selectFragment = new SelectFragment();
//        Bundle arguments = new Bundle();
//        arguments.putString("eventTag", "onBreakdownSelect");
//        arguments.putSerializable("selectObject",soBreakdown);
//        selectFragment.setArguments(arguments);
//        selectFragment.show(getSupportFragmentManager(), R.id.bottomsheet);
    }

    SelectObject soBreakdown;

@Bind(R.id.bottomsheet)
    BottomSheetLayout bottomSheetLayout;




    @Subscriber(tag = "onBreakdownSelect")
    private void onBreakdownSelect(SelectObject selectObject) {
        soBreakdown = selectObject;
        tvBreakdown.setText(selectObject.value);
        bottomSheetLayout.dismissSheet();
    }


}
