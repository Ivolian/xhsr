package unicorn.com.xhsr.groupselect;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.github.ppamorim.dragger.DraggerActivity;
import com.github.ppamorim.dragger.DraggerPosition;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.recyclerview.adapters.SlideInLeftAnimationAdapter;
import unicorn.com.xhsr.R;
import unicorn.com.xhsr.utils.ToastUtils;


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
        setSlideEnabled(false);


    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void initViews() {
        initRvMain();
        initRvSub();
    }


    // =============================== rvMain ===============================

    @Bind(R.id.rvMain)
    RecyclerView rvMain;

    MainAdapter mainAdapter;

    private void initRvMain() {
        rvMain.setLayoutManager(new LinearLayoutManager(this));
        mainAdapter = new MainAdapter();
        rvMain.setAdapter(new SlideInLeftAnimationAdapter(mainAdapter));
        rvMain.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).build());

        // 初始化主列表
        mainAdapter.level = 0;
        mainAdapter.setValueList(getValueList(0));
        mainAdapter.notifyDataSetChanged();
        mainAdapter.selectItem(0);
    }


    // =============================== rvSub ===============================

    @Bind(R.id.rvSub)
    RecyclerView rvSub;

    SubAdapter subAdapter;

    private void initRvSub() {
        rvSub.setLayoutManager(new LinearLayoutManager(this));
        subAdapter = new SubAdapter();
        rvSub.setAdapter(subAdapter);
        rvSub.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).build());

    }


    // =============================== 一大堆 ===============================

    int maxLevel = 3;

    int[] arrPositionSelected = new int[maxLevel + 1];

    @Bind({R.id.level1, R.id.level2, R.id.level3, R.id.level4})
    List<TextView> tvValueList;

    @OnClick({R.id.level1, R.id.level2, R.id.level3, R.id.level4})
    public void valueOnClick(TextView tvValue) {
        int level = tvValueList.indexOf(tvValue);
        if (level == maxLevel) {
            return;
        }
        refreshRvMain(GroupSelectHelper.create(level, arrPositionSelected[level], null));
        clearValueList(level);
    }

    private void clearValueList(int level) {
        for (int i = level + 1; i <= maxLevel; i++) {
            tvValueList.get(i).setText("");
        }
    }

    private void refreshRvSub(GroupSelectObject groupSelectObject) {
        int mainLevel = groupSelectObject.level;
        ToastUtils.show("refreshRvSub " +  mainLevel);

        subAdapter.level = mainLevel + 1;
        subAdapter.positionSelected = -1;
        subAdapter.setValueList(getValueList(subAdapter.level));
        subAdapter.notifyDataSetChanged();
        rvSub.smoothScrollToPosition(0);

    }

    private void refreshRvMain(GroupSelectObject groupSelectObject) {
        int level = groupSelectObject.level;
        int position = groupSelectObject.selectObject.position;
        mainAdapter.positionSelected = -1;
        mainAdapter.level = level;
        mainAdapter.setValueList(getValueList(level));
        mainAdapter.selectItem(position);
        rvMain.smoothScrollToPosition(position);
    }

    @Subscriber(tag = "onMainSelect")
    private void onRvMainSelect(GroupSelectObject groupSelectObject) {
        onSelect(groupSelectObject);
        refreshRvSub(groupSelectObject);
    }

    @Subscriber(tag = "onSubSelect")
    private void onRvSubSelect(GroupSelectObject groupSelectObject) {
        onSelect(groupSelectObject);
        if (groupSelectObject.level != maxLevel) {
            refreshRvMain(groupSelectObject);
        }
    }

    private void onSelect(GroupSelectObject groupSelectObject) {
        int level = groupSelectObject.level;
        int position = groupSelectObject.selectObject.position;
        String value = groupSelectObject.selectObject.value;
        if (level == 0) {
            value = "    " + value;
        } else {
            value = "  >  " + value;
        }
        tvValueList.get(level).setText(value);
        arrPositionSelected[level] = position;
    }

    private List<String> getValueList(int level) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i != 30; i++) {
            list.add("第" + (level + 1) + "层" + (i + 1));
        }
        return list;
    }

}
