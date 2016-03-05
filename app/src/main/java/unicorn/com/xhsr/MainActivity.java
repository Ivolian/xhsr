package unicorn.com.xhsr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.devspark.robototextview.widget.RobotoTextView;
import com.mikepenz.iconics.view.IconicsImageView;
import com.yo.libs.app.DimensCodeTools;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import butterknife.Bind;
import butterknife.OnClick;
import su.levenetc.android.badgeview.BadgeView;
import unicorn.com.xhsr.base.BaseActivity;
import unicorn.com.xhsr.data.BasicDataGotter;
import unicorn.com.xhsr.other.DividerGridItemDecoration;
import unicorn.com.xhsr.other.TinyDB;
import unicorn.com.xhsr.utils.SharedPreferencesUtils;
import unicorn.com.xhsr.utils.ToastUtils;
import unicorn.com.xhsr.utils.UpdateUtils;
import unicorn.com.xhsr.weather.WeatherMap;
import unicorn.com.xhsr.weather.model.WeatherInfo;

public class MainActivity extends BaseActivity {


    // =============================== onCreate ===============================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_content);
        initViews();
        UpdateUtils.checkUpdate(this);
    }

    private void initViews() {
//        initDragLayout();
        EventBus.getDefault().post(new Object(), "getBasicData");
        initWeather();
        initRecyclerView();

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


    @Subscriber(tag = "sign_out")
    public void signOut(Object o) {
        startActivityAndFinish(LoginActivity.class);
    }


    // =============================== drag layout ===============================

//    @Bind(R.id.dl)
//    DragLayout dragLayout;

//    private void initDragLayout() {
//        dragLayout.setDragListener(new DragLayout.DragListener() {
//            @Override
//            public void onOpen() {
//
//            }
//
//            @Override
//            public void onClose() {
//            }
//
//            @Override
//            public void onDrag(float percent) {
//
//            }
//        });
//    }


    // =============================== recycleview ===============================

    @Bind(R.id.recycleView)
    RecyclerView recyclerView;

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.addItemDecoration(new DividerGridItemDecoration(this));
        recyclerView.setAdapter(new MainAdapter());
    }


    // =============================== onClick ===============================


    @OnClick(R.id.scan)
    public void scanOnClick() {
        DimensCodeTools.startScan(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String res = DimensCodeTools.scanForResult(requestCode, resultCode, data);
        if (res != null) {
            ToastUtils.show("设备码: " + res);
        }
    }


    @Bind(R.id.temperature)
    RobotoTextView tvTemperature;

    @Bind(R.id.pm25)
    BadgeView bvPm25;

    @Bind(R.id.quality)
    TextView tvQuality;

    @Bind(R.id.weather)
    TextView tvWeather;

    @Bind(R.id.weather_icon)
    IconicsImageView weatherIcon;

    private void initWeather() {
        WeatherInfo weatherInfo = (WeatherInfo) TinyDB.getInstance().getObject(SharedPreferencesUtils.WEATHER_INFO, WeatherInfo.class);
        if (weatherInfo != null) {
            tvTemperature.setText(weatherInfo.getNow().getTmp() + "\u00B0");
            bvPm25.setValue(weatherInfo.getAqi().getCity().getPm25(), false);
            tvQuality.setText(weatherInfo.getAqi().getCity().getQlty());
            String txt = weatherInfo.getNow().getCond().getTxt();
            tvWeather.setText("东城区 " + txt);
            String icon = WeatherMap.getMap().get(txt);
            if (icon == null) {
                icon = WeatherMap.getMap().get("未知");
            }
            weatherIcon.setIcon(icon);
        }
    }


}
