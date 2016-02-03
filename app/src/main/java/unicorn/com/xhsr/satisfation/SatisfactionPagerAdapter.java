package unicorn.com.xhsr.satisfation;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


public class SatisfactionPagerAdapter extends FragmentStatePagerAdapter {


    public SatisfactionPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {

        if (position == getCount()-1){
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
        return 10 + 10 + 2 + 3 + 1;
    }


}
