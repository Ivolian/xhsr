package unicorn.com.xhsr.satisfaction;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.f2prateek.dart.InjectExtra;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.NormalListDialog;

import org.json.JSONArray;
import org.json.JSONObject;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import unicorn.com.xhsr.DialogUtils;
import unicorn.com.xhsr.R;
import unicorn.com.xhsr.SimpleApplication;
import unicorn.com.xhsr.base.BaseActivity;
import unicorn.com.xhsr.data.greendao.SatisfactionOption;
import unicorn.com.xhsr.data.greendao.SatisfactionOptionDao;
import unicorn.com.xhsr.other.ClickHelp;
import unicorn.com.xhsr.satisfaction.model.SatisfactionResult;
import unicorn.com.xhsr.utils.ConfigUtils;
import unicorn.com.xhsr.utils.ToastUtils;
import unicorn.com.xhsr.volley.SimpleVolley;


public class SatisfactionActivity extends BaseActivity {


    // ============================ extra ============================

    @InjectExtra("satisfactionResult")
    SatisfactionResult satisfactionResult;


    // ============================ views ============================

    @Bind(R.id.title)
    TextView title;

    @Bind(R.id.numerator)
    TextView numerator;

    @Bind(R.id.denominator)
    TextView denominator;

    @Bind(R.id.viewpager)
    ViewPager viewPager;


    // ============================ onCreate ============================

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_satisfaction);
        initViews();
    }

    private void initViews() {
        initViewpager();
    }


    // ============================ initViewpager ============================

    SatisfactionPagerAdapter satisfactionPagerAdapter;

    private void initViewpager() {
        satisfactionPagerAdapter = new SatisfactionPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(satisfactionPagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                // 若是建议界面
                if (position == satisfactionPagerAdapter.getCount() - 1) {
                    title.setText(getServiceNames().length + ". 其他建议与意见");
                    numerator.setVisibility(View.INVISIBLE);
                    denominator.setVisibility(View.INVISIBLE);
                    return;
                }
                // 若是选项界面
                numerator.setVisibility(View.VISIBLE);
                denominator.setVisibility(View.VISIBLE);
                notifyOptionChange(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setCurrentItem(0);
        // 好像不会触发 onPageSelected，所以得手动调用下
        notifyOptionChange(0);
    }

    private void notifyOptionChange(int orderNo) {
        SatisfactionOption option = SimpleApplication.getDaoSession().getSatisfactionOptionDao().queryBuilder()
                .where(SatisfactionOptionDao.Properties.OrderNo.eq(orderNo))
                .unique();
        title.setText(option.getTitle());
        numerator.setText(option.getNumerator() + "");
        denominator.setText("/" + option.getDenominator() + "");
    }


    // ============================ optionOnSelect ============================

    @Subscriber(tag = "optionOnSelect")
    public void optionOnSelect(Integer position) {
        viewPager.setCurrentItem(position + 1, true);
    }


    // ============================ 提交问卷 ============================

    @Subscriber(tag = "submitOnClick")
    public void submitOnClick(String advice) {
        // 确认所有选项都已评分
//        SatisfactionOption option = SimpleApplication.getDaoSession().getSatisfactionOptionDao().queryBuilder()
//                .where(SatisfactionOptionDao.Properties.Score.eq(-1))
//                .orderAsc(SatisfactionOptionDao.Properties.OrderNo)
//                .limit(1)
//                .unique();
//        if (option != null) {
//            viewPager.setCurrentItem(option.getOrderNo(), true);
//            ToastUtils.show("尚有条目未评分");
//        } else {
        satisfactionResult.setAdvice(advice);
        commitSatisfactionResult();
//        }
    }

    private void commitSatisfactionResult() {
        String url = ConfigUtils.getBaseUrl() + "/api/v1/hems/assess";
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ToastUtils.show("问卷已提交");
                        clearScore();
                        finish();
                    }
                },
                SimpleVolley.getDefaultErrorListener()
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("Cookie", "JSESSIONID=" + ConfigUtils.getSessionId());
                // 不加这个会出现 Unsupported media type 415 错误
                map.put("Content-Type", "application/json");
                return map;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                List<SatisfactionOption> optionList = SimpleApplication.getDaoSession().getSatisfactionOptionDao().queryBuilder()
                        .orderAsc(SatisfactionOptionDao.Properties.OrderNo)
                        .list();
                try {
                    JSONObject result = new JSONObject();
                    result.put("phone", satisfactionResult.getPhone());
                    result.put("username", satisfactionResult.getUsername());
                    result.put("assessDate", satisfactionResult.getAssessDate());
                    result.put("advice", satisfactionResult.getAdvice());

                    JSONObject department = new JSONObject();
                    department.put("objectId", satisfactionResult.getDepartmentId());
                    result.put("department", department);

                    // 下面的代码有点伤，不用在意
                    JSONArray jsonArray = new JSONArray();
                    for (SatisfactionOption option : optionList) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("score", option.getScore());
                        JSONObject content = new JSONObject();
                        content.put("objectId", option.getObjectId());
                        jsonObject.put("content", content);
                        jsonArray.put(jsonObject);
                    }
                    result.put("contentList", jsonArray);
                    String jsonString = result.toString();
                    return jsonString.getBytes("UTF-8");
                } catch (Exception e) {
                    //
                }
                return null;
            }
        };
        SimpleVolley.addRequest(stringRequest);
    }

    private void clearScore() {
        List<SatisfactionOption> optionList = SimpleApplication.getDaoSession().getSatisfactionOptionDao().loadAll();
        for (SatisfactionOption option : optionList) {
            option.setScore(-1);
        }
        SimpleApplication.getDaoSession().getSatisfactionOptionDao().updateInTx(optionList);
    }

    // ============================ 更多操作 -> 问卷目录 ============================

    @OnClick(R.id.more)
    public void moreOnClick(View view) {
        if (ClickHelp.isFastClick()) {
            return;
        }
        showPopupMenu();
    }

    private void showPopupMenu() {
        PopupMenu popupMenu = new PopupMenu(this, findViewById(R.id.more));
        popupMenu.inflate(R.menu.satisfaction);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.category) {
                    showCategory();
                }
                return false;
            }
        });
        popupMenu.show();
    }

    private void showCategory() {
        final String[] serviceNames = getServiceNames();
        final NormalListDialog normalListDialog = new NormalListDialog(SatisfactionActivity.this, serviceNames);
        normalListDialog.title("问卷目录");
        normalListDialog.titleTextSize_SP(20);
        normalListDialog.itemTextSize(16);
        normalListDialog.titleBgColor(ContextCompat.getColor(SatisfactionActivity.this, R.color.colorPrimary));
        normalListDialog.cornerRadius(0);
        normalListDialog.layoutAnimation(null);
        normalListDialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 其他建议与意见
                if (position == serviceNames.length - 1) {
                    viewPager.setCurrentItem(satisfactionPagerAdapter.getCount());
                    normalListDialog.dismiss();
                    return;
                }

                // 其他服务
                SatisfactionOption option = SimpleApplication.getDaoSession().getSatisfactionOptionDao().queryBuilder()
                        .where(SatisfactionOptionDao.Properties.Title.eq(serviceNames[position]))
                        .orderAsc(SatisfactionOptionDao.Properties.OrderNo)
                        .limit(1)
                        .unique();
                viewPager.setCurrentItem(option.getOrderNo(), true);
                normalListDialog.dismiss();
            }
        });
        normalListDialog.show();
    }

    private String[] getServiceNames() {
        List<SatisfactionOption> optionList = SimpleApplication.getDaoSession().getSatisfactionOptionDao().loadAll();
        final List<String> serviceNameList = new ArrayList<>();
        for (SatisfactionOption option : optionList) {
            String serviceName = option.getTitle();
            if (!serviceNameList.contains(serviceName)) {
                serviceNameList.add(serviceName);
            }
        }
        serviceNameList.add((serviceNameList.size() + 1) + ". 其他建议与意见");

        String[] serviceNames = new String[serviceNameList.size()];
        serviceNameList.toArray(serviceNames);
        return serviceNames;
    }


    // ================================ cancel ================================

    @OnClick(R.id.cancel)
    public void cancelOnClick() {
        if (ClickHelp.isFastClick()) {
            return;
        }
        DialogUtils.showConfirm(this, "确认退出？",
                new DialogUtils.Action() {
                    @Override
                    public void doAction() {
                        finish();
                    }
                },
                new DialogUtils.Action() {
                    @Override
                    public void doAction() {
                        // do nothing
                    }
                }
        );
    }


}
