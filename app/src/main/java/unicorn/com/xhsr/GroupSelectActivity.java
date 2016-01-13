package unicorn.com.xhsr;

import android.os.Bundle;

import com.github.ppamorim.dragger.DraggerActivity;
import com.github.ppamorim.dragger.DraggerPosition;

import org.simple.eventbus.EventBus;

import butterknife.ButterKnife;


public class GroupSelectActivity extends DraggerActivity {


    // =============================== onCreate & onDestroy ===============================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_group_select);
        ButterKnife.bind(this);
        initViews();
        setFriction(10);
        setDraggerPosition(DraggerPosition.RIGHT);

    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void initViews() {

    }



}
