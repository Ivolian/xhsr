package unicorn.com.xhsr.orderdetail;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


public class OrderDetailPagerAdapter extends FragmentStatePagerAdapter {

    public static String[] titles = {"工单流程", "设备明细"};

    public OrderDetailPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        return new OrderFlowFragment();
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

}
