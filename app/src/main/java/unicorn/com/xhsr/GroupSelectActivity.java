package unicorn.com.xhsr;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.github.ppamorim.dragger.DraggerActivity;
import com.github.ppamorim.dragger.DraggerPosition;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.simple.eventbus.EventBus;

import butterknife.Bind;
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
        initRvMain();
    }

    //

    @Bind(R.id.rvMain)
    RecyclerView rvMain;

    private void initRvMain(){
        rvMain.setLayoutManager(new LinearLayoutManager(this));
        rvMain.setAdapter(new GroupMainAdapter());
        rvMain.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).build());
    }

}
