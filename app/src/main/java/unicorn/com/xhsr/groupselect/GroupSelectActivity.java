package unicorn.com.xhsr.groupselect;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import com.f2prateek.dart.Dart;
import com.f2prateek.dart.InjectExtra;
import com.github.ppamorim.dragger.DraggerActivity;
import com.github.ppamorim.dragger.DraggerPosition;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;
import com.zhy.android.percent.support.PercentLinearLayout;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.recyclerview.adapters.SlideInLeftAnimationAdapter;
import unicorn.com.xhsr.R;


public class GroupSelectActivity extends DraggerActivity {

    @InjectExtra("maxLevel")
    Integer maxLevel;

    @Bind(R.id.result_container)
    PercentLinearLayout resultContainer;

    // =============================== onCreate & onDestroy ===============================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_group_select);
        ButterKnife.bind(this);
        Dart.inject(this);

        initViews();

        setSlideEnabled(false);
        setTension(1);
        setDraggerPosition(DraggerPosition.RIGHT);
      arrPositionSelected = new int[maxLevel + 1];




        tvValueList = new ArrayList<>();
       for (int i=0;i<=maxLevel;i++) {
           TextView textView = new TextView(this);
           textView.setTextColor(getResources().getColor(R.color.md_blue_grey_500));
           textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,16);
           ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
           resultContainer.addView(textView, layoutParams);
           tvValueList.add(textView);
           textView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   valueOnClick(v);
               }
           });
       }
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

    @OnClick(R.id.cancel)
    public void cancel(){
        closeActivity();
    }

    @OnClick(R.id.confirm)
    public void confirm(){
        Intent intent =new Intent();
        intent.putExtra("result",getResult());
        setResult(2333,intent);
        closeActivity();
    }

    private String getResult(){
        String result = "";
        for (TextView tvValue:tvValueList){
            result  += tvValue.getText().toString();
        }
        return  result.replace(">","");
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
        subAdapter = new SubAdapter(maxLevel);
        rvSub.setAdapter(subAdapter);
        rvSub.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).build());

    }


    // =============================== 一大堆 ===============================


    int[] arrPositionSelected;


    List<TextView> tvValueList;


    public void valueOnClick(View tvValue) {
        int level = tvValueList.indexOf(tvValue);
        if (level == maxLevel) {
            return;
        }
        refreshRvMain(GroupSelectHelper.create(level, arrPositionSelected[level], null));

    }

    private void clearValueList(int level) {
        for (int i = level + 1; i <= maxLevel; i++) {
            tvValueList.get(i).setText("");
        }
    }

    private void refreshRvSub(GroupSelectObject groupSelectObject) {
        int mainLevel = groupSelectObject.level;

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
        clearValueList(groupSelectObject.level);

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
            value = value;
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
