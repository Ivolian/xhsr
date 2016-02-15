package unicorn.com.xhsr.groupselect;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.f2prateek.dart.InjectExtra;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.simple.eventbus.Subscriber;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import jp.wasabeef.recyclerview.adapters.SlideInLeftAnimationAdapter;
import unicorn.com.xhsr.R;
import unicorn.com.xhsr.base.BaseActivity;
import unicorn.com.xhsr.select.SelectObject;
import unicorn.com.xhsr.utils.ToastUtils;

public class GroupSelectActivity extends BaseActivity {


    /*
        GroupSelect 分为 4 块部分
        1. 查询框
        2. 查询结果列表
        3. 主列表
        4. 副列表
     */


    // =============================== data provider ===============================

    public static DataProvider dataProvider;

    public interface DataProvider {

        List<SelectObject> getMainDataList();

        List<SelectObject> getSubDataList(String mainId);

        List<SelectObject> getSearchResultDataList(String query);

    }


    // =============================== extra ===============================

    @InjectExtra("name")
    String name;

    @Nullable
    @InjectExtra("mainId")
    String mainId;

    @Nullable
    @InjectExtra("subId")
    String subId;

    @InjectExtra("resultCode")
    Integer resultCode;


    // =============================== onCreate & onDestroy ===============================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_select);
        initViews();
    }

    private void initViews() {
        initTitle();
        initSearchBox();
        initRvSearchResult();
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
    ImageView clear;

    private void initSearchBox() {
        etSearch.setHint("输入" + name + "相关信息");
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence input, int start, int before, int count) {
                if (TextUtils.isEmpty(input)) {
                    clear.setVisibility(View.INVISIBLE);
                    rvSearchResult.setVisibility(View.INVISIBLE);
                } else {
                    clear.setVisibility(View.VISIBLE);
                    List<SelectObject> dataList = dataProvider.getSearchResultDataList(input.toString());
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
    private void onSearchResultSelect(String subId) {
        finishAfterSetResult(subId);
    }

    private void finishAfterSetResult(String objectId) {
        Intent data = new Intent();
        data.putExtra("objectId", objectId);
        setResult(resultCode, data);
        finish();
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
        mainAdapter.setDataList(dataProvider.getMainDataList());
            mainAdapter.selectItem(0);
    }


    // =============================== 副列表 ===============================

    @Bind(R.id.rvSub)
    RecyclerView rvSub;

    SubAdapter subAdapter;

    private void initRvSub() {
        rvSub.setLayoutManager(new LinearLayoutManager(this));
        subAdapter = new SubAdapter();
        rvSub.setAdapter(subAdapter);
        rvSub.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).build());

    }


    // =============================== onSelect ===============================

    @Subscriber(tag = "onMainSelect")
    private void onRvMainSelect(String mainId) {
        subId = null;
        subAdapter.positionSelected = -1;
        subAdapter.setDataList(dataProvider.getSubDataList(mainId));
        subAdapter.notifyDataSetChanged();
        rvSub.smoothScrollToPosition(0);


    }

    @Subscriber(tag = "onSubSelect")
    private void onRvSubSelect(String subId) {
        this.subId = subId;
    }


    // =============================== 基础方法 ===============================

    @OnClick(R.id.cancel)
    public void cancel() {
        finish();
    }

    @OnClick(R.id.confirm)
    public void confirm() {
        if (subId == null) {
            ToastUtils.show("请先选择" + name);
            return;
        }
        finishAfterSetResult(subId);
    }




}
