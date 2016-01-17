package unicorn.com.xhsr.groupselect;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.f2prateek.dart.InjectExtra;
import com.github.ppamorim.dragger.DraggerActivity;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;
import com.zhy.android.percent.support.PercentLinearLayout;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import jp.wasabeef.recyclerview.adapters.SlideInLeftAnimationAdapter;
import unicorn.com.xhsr.ActivityHelp;
import unicorn.com.xhsr.R;
import unicorn.com.xhsr.select.SelectObject;


public class GroupSelectActivity extends DraggerActivity {

    /*
        GroupSelect 分为 5 块部分
        1. 查询框
        2. 查询结果列表
        3. 选择结果
        4. 主列表
        5. 副列表
     */


    // =============================== data provider ===============================

    public static DataProvider dataProvider;


    public interface DataProvider {

        List<SelectObject> getMainDataList();

        List<SelectObject> getSubDataList(SelectObject selectObject);

        List<SelectObject> getSearchResultDataList(String query);

    }


    // =============================== extra ===============================

    @InjectExtra("name")
    String name;

    @InjectExtra("resultCode")
    Integer resultCode;

    Integer maxLevel = 1;


    // =============================== onCreate & onDestroy ===============================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_group_select);
        ActivityHelp.initActivity(this);
        initViews();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void initViews() {
        initTitle();
        initSearchBox();
        initRvSearchResult();
        initSelectResult();
        initRvMain();
        initRvSub();
    }


    // =============================== 标题 ===============================

    @Bind(R.id.title)
    TextView tvTitle;

    private void initTitle() {
        String title = "选择" + name;
        tvTitle.setText(title);
    }


    // =============================== 查询框部分 ===============================

    @Bind(R.id.search)
    EditText etSearch;

    @Bind(R.id.clear)
    ImageView ivClear;

    private void initSearchBox() {
        etSearch.setHint("输入" + name + "相关信息");
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean empty = TextUtils.isEmpty(s);
                if (empty) {
                    ivClear.setVisibility(View.INVISIBLE);
                    rvSearchResult.setVisibility(View.INVISIBLE);
                } else {
                    ivClear.setVisibility(View.VISIBLE);
                    List<SelectObject> dataList = dataProvider.getSearchResultDataList(s.toString());
                    if (dataList.isEmpty()) {
                        rvSearchResult.setVisibility(View.INVISIBLE);
                    } else {
                        rvSearchResult.setVisibility(View.VISIBLE);
                        searchResultAdapter.setDataList(dataList);
                        searchResultAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @OnClick(R.id.clear)
    public void clearOnClick() {
        etSearch.setText("");
    }

    @Override
    public void onBackPressed() {
        if (rvSearchResult.getVisibility() == View.VISIBLE) {
            rvSearchResult.setVisibility(View.INVISIBLE);
        } else {
            super.onBackPressed();
        }
    }


    // =============================== 查询结果列表部分 ===============================

    @Bind(R.id.rvSearchResult)
    RecyclerView rvSearchResult;

    SearchResultAdapter searchResultAdapter;

    private void initRvSearchResult() {
        rvSearchResult.setLayoutManager(new LinearLayoutManager(this));
        searchResultAdapter = new SearchResultAdapter();
        rvSearchResult.setAdapter(searchResultAdapter);
        rvSearchResult.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).build());
    }

    @Subscriber(tag = "onSearchResultSelect")
    private void onSearchResultSelect(SelectObject selectObject) {
        finishAfterSetResult(selectObject.value);
    }

    private void finishAfterSetResult(String result) {
        Intent intent = new Intent();
        intent.putExtra("result", result);
        setResult(resultCode, intent);
        closeActivity();
    }


    // =============================== 选择结果部分 ===============================

    List<TextView> tvValueList;

    int[] positionHandler;

    @Bind(R.id.selectResultContainer)
    PercentLinearLayout selectResultContainer;

    private void initSelectResult() {
        tvValueList = new ArrayList<>();
        positionHandler = new int[maxLevel + 1];
        for (int i = 0; i <= maxLevel; i++) {
            TextView textView = new TextView(this);
            textView.setTextColor(ContextCompat.getColor(this, R.color.md_blue_grey_500));
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    valueOnClick(v);
                }
            });
            selectResultContainer.addView(textView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            tvValueList.add(textView);
        }
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    private void valueOnClick(View tvValue) {
        int level = tvValueList.indexOf(tvValue);
        if (level == maxLevel) {
            return;
        }
        refreshRvMain(positionHandler[level]);
    }

    @OnClick(R.id.confirm)
    public void confirm() {
        finishAfterSetResult(getSelectResult());
    }

    private String getSelectResult() {
        String result = "";
        for (TextView tvValue : tvValueList) {
            result += tvValue.getText().toString();
        }
        return result;
    }


    // =============================== 主列表 ===============================

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
        mainAdapter.setDataList(dataProvider.getMainDataList());
        mainAdapter.selectItem(0);
    }


    // =============================== 副列表 ===============================

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


    @Subscriber(tag = "onMainSelect")
    private void onRvMainSelect(GroupSelectObject groupSelectObject) {
        refreshSelectResult(groupSelectObject);
        refreshRvSub(groupSelectObject);
    }

    @Subscriber(tag = "onSubSelect")
    private void onRvSubSelect(GroupSelectObject groupSelectObject) {
        refreshSelectResult(groupSelectObject);
    }

    private void refreshSelectResult(GroupSelectObject groupSelectObject) {
        int level = groupSelectObject.level;
        int position = groupSelectObject.selectObjectWithPosition.position;
        String value = groupSelectObject.selectObjectWithPosition.value;
        if (level != 0) {
            value = "  /  " + value;
        }
        tvValueList.get(level).setText(value);
        positionHandler[level] = position;
    }

    private void refreshRvMain(int positionSelected) {
        mainAdapter.setDataList(dataProvider.getMainDataList());
        mainAdapter.selectItem(positionSelected);
        rvMain.smoothScrollToPosition(positionSelected);
    }

    private void refreshRvSub(GroupSelectObject groupSelectObject) {
        int mainLevel = groupSelectObject.level;
        clearSelectResult(mainLevel);
        subAdapter.level = mainLevel + 1;
        subAdapter.positionSelected = -1;
        subAdapter.setDataList(dataProvider.getSubDataList(groupSelectObject.selectObjectWithPosition));
        subAdapter.notifyDataSetChanged();
        rvSub.smoothScrollToPosition(0);
    }

    private void clearSelectResult(int level) {
        for (int i = level + 1; i <= maxLevel; i++) {
            tvValueList.get(i).setText("");
        }
    }


    // =============================== 基础方法 ===============================

    @OnClick(R.id.cancel)
    public void cancel() {
        closeActivity();
    }


}
