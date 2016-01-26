package unicorn.com.xhsr.myorder;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.f2prateek.dart.InjectExtra;

import butterknife.Bind;
import unicorn.com.xhsr.R;
import unicorn.com.xhsr.base.BaseActivity;


public class MyOrderActivity extends BaseActivity {


    // =============================== extra ===============================

    @Nullable
    @InjectExtra("currentItem")
    Integer currentItem;


    // =============================== views ===============================

    @Bind(R.id.tab_layout)
    TabLayout tabLayout;

    @Bind(R.id.viewpager)
    ViewPager viewPager;


    // =============================== onCreate ===============================

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        initViews();
    }

    public void initViews() {
        initViewPaper();
        iniTabLayout();
    }

    private void initViewPaper() {
        PagerAdapter pagerAdapter = new MyOrderPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(pagerAdapter.getCount());
        if (currentItem != null) {
            viewPager.setCurrentItem(currentItem);
        }
    }

    private void iniTabLayout() {
        int color = ContextCompat.getColor(this, R.color.md_black);
        tabLayout.setTabTextColors(color, color);
        tabLayout.setSelectedTabIndicatorHeight(10);
        tabLayout.setupWithViewPager(viewPager);
    }

}
