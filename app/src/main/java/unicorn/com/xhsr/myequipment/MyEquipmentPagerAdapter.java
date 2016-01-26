package unicorn.com.xhsr.myequipment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


public class MyEquipmentPagerAdapter extends FragmentStatePagerAdapter {

    public static String[] titles = {
            "待维修", "维修中", "已维修",
    };

    public MyEquipmentPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        return new WaitRepairFragment();
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
