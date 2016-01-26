package unicorn.com.xhsr.myequipment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.f2prateek.dart.InjectExtra;

import butterknife.Bind;
import unicorn.com.xhsr.R;
import unicorn.com.xhsr.base.BaseActivity;


public class MyEquipmentActivity extends BaseActivity {

    @Bind(R.id.tab_layout)
    TabLayout tabLayout;

    @Bind(R.id.viewpager)
    ViewPager viewPager;

    @Nullable
    @InjectExtra("currentItem")
    Integer currentItem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_equipment);
        initViews();
    }

    public void initViews() {
        MyEquipmentPagerAdapter pagerAdapter = new MyEquipmentPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(pagerAdapter.getCount());
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setSelectedTabIndicatorHeight(10);
        int color = getResources().getColor(R.color.md_black);
        tabLayout.setTabTextColors(color, color);

        if (currentItem != null) {
            viewPager.setCurrentItem(currentItem);
        }
    }

}
