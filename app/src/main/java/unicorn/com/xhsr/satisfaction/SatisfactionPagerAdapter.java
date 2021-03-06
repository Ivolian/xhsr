package unicorn.com.xhsr.satisfaction;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import unicorn.com.xhsr.SimpleApplication;


public class SatisfactionPagerAdapter extends FragmentStatePagerAdapter {

    public SatisfactionPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == getCount() - 1) {
            return new AdviceFragment();
        }

        SatisfactionFragment satisfactionFragment = new SatisfactionFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        satisfactionFragment.setArguments(args);
        return satisfactionFragment;
    }

    @Override
    public int getCount() {
        return (int) SimpleApplication.getDaoSession().getSatisfactionOptionDao().count() + 1;
    }

}
