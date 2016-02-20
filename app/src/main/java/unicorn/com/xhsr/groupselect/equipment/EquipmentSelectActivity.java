package unicorn.com.xhsr.groupselect.equipment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
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
import com.tonicartos.superslim.LayoutManager;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.simple.eventbus.Subscriber;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import unicorn.com.xhsr.R;
import unicorn.com.xhsr.base.BaseActivity;
import unicorn.com.xhsr.data.DataHelp;
import unicorn.com.xhsr.goodwork.fastscroll.FastScrollRecyclerView;
import unicorn.com.xhsr.goodwork.fastscroll.FastScrollRecyclerViewItemDecoration;
import unicorn.com.xhsr.groupselect.GroupSelectActivity;
import unicorn.com.xhsr.groupselect.SearchResultAdapter;
import unicorn.com.xhsr.groupselect.SubAdapter;
import unicorn.com.xhsr.select.SelectObject;
import unicorn.com.xhsr.utils.ResultCodeUtils;
import unicorn.com.xhsr.utils.ToastUtils;


public class EquipmentSelectActivity extends BaseActivity {


    /*
        GroupSelect 分为 4 块部分
        1. 查询框
        2. 查询结果列表
        3. 主列表
        4. 副列表
     */


    // =============================== data provider ===============================

    private GroupSelectActivity.DataProvider dataProvider = DataHelp.getEquipmentDataProvider();


    // =============================== extra ===============================

    @Nullable
    @InjectExtra("subId")
    String subId;


    // =============================== onCreate ===============================

    boolean needInit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment_select);
        initViews();
    }

    private void initViews() {
        needInit = (subId != null);
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
        tvTitle.setText("选择设备");
    }


    // =============================== 查询框部分 ===============================

    @Bind(R.id.search)
    EditText etSearch;

    @Bind(R.id.clear)
    ImageView clear;

    private void initSearchBox() {
        etSearch.setHint("输入设备相关信息");
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
                    List<SelectObject> searchResultDataList = dataProvider.getSearchResultDataList(input.toString());
                    if (searchResultDataList.isEmpty()) {
                        rvSearchResult.setVisibility(View.INVISIBLE);
                    } else {
                        rvSearchResult.setVisibility(View.VISIBLE);
                        searchResultAdapter.setDataList(searchResultDataList);
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
        finishWithResult(subId);
    }

    private void finishWithResult(String objectId) {
        Intent data = new Intent();
        data.putExtra("subId", objectId);
        setResult(ResultCodeUtils.EQUIPMENT, data);
        finish();
    }


    // =============================== 主列表 ===============================

    @Bind(R.id.rvMain)
    FastScrollRecyclerView rvMain;

    private HashMap<String, Integer> calculateIndexesForName(List<SelectObject> items) {
        HashMap<String, Integer> mapIndex = new LinkedHashMap<>();
        for (int i = 0; i < items.size(); i++) {
            String name = items.get(i).value;
            String index = name.substring(0, 1);
            index = index.toUpperCase();

            if (!mapIndex.containsKey(index)) {
                mapIndex.put(index, i);
            }
        }
        return mapIndex;
    }

    private void initRvMain() {
        List<SelectObject> mainDataList = dataProvider.getMainDataList();


        EquipmentAdapter equipmentAdapter = new EquipmentAdapter(mainDataList);
        rvMain.setLayoutManager(new LayoutManager(this));
        rvMain.setAdapter(equipmentAdapter);

        FastScrollRecyclerViewItemDecoration decoration = new FastScrollRecyclerViewItemDecoration(this);
        rvMain.addItemDecoration(decoration);
        rvMain.setItemAnimator(new DefaultItemAnimator());

        // 如果没有选中项，默认选择第一个
        if (!needInit) {
            equipmentAdapter.selectItem(1);
            return;
        }

        // 如果有选中项
        String mainId = dataProvider.getMainId(subId);
        int position = equipmentAdapter.getPositionByMainId(mainId);
        equipmentAdapter.selectItem(position);
        rvMain.scrollToPosition(position);
    }

    @Subscriber(tag = "onMainSelect")
    private void onRvMainSelect(String mainId) {
        List<SelectObject> subDataList = dataProvider.getSubDataList(mainId);
        if (needInit) {
            subAdapter.refreshDataList(subDataList);
            for (SelectObject selectObject : subDataList) {
                if (selectObject.objectId.equals(subId)) {
                    int index = subDataList.indexOf(selectObject);
                    subAdapter.selectItem(index);
                    rvSub.smoothScrollToPosition(index);
                }
            }
            needInit = false;
            return;
        }

        //
        subId = null;
        subAdapter.refreshDataList(subDataList);
        rvSub.smoothScrollToPosition(0);
    }


    // =============================== 副列表 ===============================

    @Bind(R.id.rvSub)
    RecyclerView rvSub;

    SubAdapter subAdapter;

    private void initRvSub() {
        subAdapter = new SubAdapter();
        rvSub.setAdapter(subAdapter);
        rvSub.setLayoutManager(new LinearLayoutManager(this));
//        rvSub.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).build());
    }

    @Subscriber(tag = "onSubSelect")
    private void onRvSubSelect(String subId) {
        this.subId = subId;
    }


    // =============================== cancel & confirm ===============================

    @OnClick(R.id.cancel)
    public void cancel() {
        finish();
    }

    @OnClick(R.id.confirm)
    public void confirm() {
        if (subId == null) {
            ToastUtils.show("请先选择设备");
            return;
        }
        finishWithResult(subId);
    }


}
