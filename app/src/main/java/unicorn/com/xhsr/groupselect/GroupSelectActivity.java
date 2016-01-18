package unicorn.com.xhsr.groupselect;

import android.content.Intent;
import android.os.Bundle;
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
import com.github.ppamorim.dragger.DraggerActivity;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import jp.wasabeef.recyclerview.adapters.SlideInLeftAnimationAdapter;
import unicorn.com.xhsr.ActivityHelp;
import unicorn.com.xhsr.R;
import unicorn.com.xhsr.select.SelectObject;
import unicorn.com.xhsr.select.SelectObjectWithPosition;


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
        finishAfterSetResult(selectObject);
    }

    private void finishAfterSetResult(SelectObject selectObject) {
        Intent intent = new Intent();
        intent.putExtra("result", selectObject);
        setResult(resultCode, intent);
        closeActivity();
    }


    // =============================== 选择结果部分 ===============================

    @Bind(R.id.selectResultMain)
    TextView tvSelectResultMain;

    @Bind(R.id.selectResultSub)
    TextView tvSelectResultSub;

    SelectObjectWithPosition selectObjectMain;

    SelectObjectWithPosition selectObjectSub;

    @OnClick(R.id.confirm)
    public void confirm() {
        SelectObject selectObject = new SelectObject();
        selectObject.value = selectObjectMain.value + " / " + selectObjectSub.value;
        selectObject.objectId = selectObjectSub.objectId;
        finishAfterSetResult(selectObject);
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
    private void onRvMainSelect(SelectObjectWithPosition selectObjectWithPosition) {
        selectObjectMain = selectObjectWithPosition;
        tvSelectResultMain.setText(selectObjectWithPosition.value);
        tvSelectResultSub.setText("");
        subAdapter.positionSelected = -1;
        subAdapter.setDataList(dataProvider.getSubDataList(selectObjectWithPosition));
        subAdapter.notifyDataSetChanged();
        rvSub.smoothScrollToPosition(0);
    }

    @Subscriber(tag = "onSubSelect")
    private void onRvSubSelect(SelectObjectWithPosition selectObjectWithPosition) {
        tvSelectResultSub.setText(selectObjectWithPosition.value);
        selectObjectSub = selectObjectWithPosition;
    }


    // =============================== 基础方法 ===============================

    @OnClick(R.id.cancel)
    public void cancel() {
        closeActivity();
    }


}
