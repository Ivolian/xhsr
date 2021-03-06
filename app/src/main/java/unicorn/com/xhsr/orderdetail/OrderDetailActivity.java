package unicorn.com.xhsr.orderdetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import butterknife.Bind;
import unicorn.com.xhsr.R;
import unicorn.com.xhsr.base.BaseActivity;


public class OrderDetailActivity extends BaseActivity{


    // =============================== views ===============================

    @Bind(R.id.tab_layout)
    TabLayout tabLayout;

    @Bind(R.id.viewpager)
    ViewPager viewPager;


    // =============================== onCreate ===============================

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        initViews();
    }

    public void initViews() {
        initViewPaper();
        iniTabLayout();
    }

    private void initViewPaper() {
        PagerAdapter pagerAdapter = new OrderDetailPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(pagerAdapter.getCount());
    }

    private void iniTabLayout() {
        int color = ContextCompat.getColor(this, R.color.md_black);
        tabLayout.setTabTextColors(color, color);
        tabLayout.setSelectedTabIndicatorHeight(10);
        tabLayout.setupWithViewPager(viewPager);
    }

}
