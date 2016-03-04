package unicorn.com.xhsr;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.mikepenz.iconics.view.IconicsImageView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import unicorn.com.xhsr.data.greendao.SatisfactionOption;
import unicorn.com.xhsr.detailorder.DetailOrderActivity;
import unicorn.com.xhsr.other.ClickHelp;
import unicorn.com.xhsr.other.TinyDB;
import unicorn.com.xhsr.quickorder.QuickOrderActivity;
import unicorn.com.xhsr.satisfaction.SatisfactionActivity;
import unicorn.com.xhsr.utils.ConfigUtils;
import unicorn.com.xhsr.utils.DialogUtils;
import unicorn.com.xhsr.utils.SfUtils;
import unicorn.com.xhsr.utils.ToastUtils;
import unicorn.com.xhsr.volley.JSONObjectRequestWithSessionCheck;
import unicorn.com.xhsr.volley.SimpleVolley;


public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {


    // ================================== values ==================================

    private String[] texts = {"快速下单", "详细下单", "满意度调查", "用户登出", "楼层平面", "故障统计", "联系客服", "添加更多"};

    private String[] icons = {"gmi-assignment-check", "gmi-assignment", "gmi-flower-alt", "faw-sign-out", "gmi-home", "gmi-wrench", "gmi-whatsapp", "gmi-plus"};

    private int[] colors = {R.color.md_teal_400, R.color.md_brown_400, R.color.md_red_300, R.color.md_light_blue_500};

    private int[] contourColors = {R.color.md_teal_500, R.color.md_brown_500, R.color.md_red_400, R.color.md_light_blue_600};

    public MainAdapter() {
        if (texts.length != icons.length) {
            throw new RuntimeException("文本和图标数组长度不一致!");
        }
    }


    // ================================== onCreateViewHolder ==================================

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.text)
        TextView tvText;

        @Bind(R.id.icon)
        IconicsImageView iivIcon;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.item)
        public void itemOnClick() {
            if (ClickHelp.isFastClick()) {
                return;
            }
            final Intent intent;
            Context context = tvText.getContext();
            switch (getAdapterPosition()) {
                case 0:
                    intent = new Intent(context, QuickOrderActivity.class);
                    context.startActivity(intent);
                    break;
                case 1:
                    intent = new Intent(context, DetailOrderActivity.class);
                    context.startActivity(intent);
                    break;
                case 2:
                    getOption(tvText.getContext());
                    break;
                case 3:
                    DialogUtils.showConfirm(context, "确认登出？", new DialogUtils.Action() {
                        @Override
                        public void doAction() {
                            EventBus.getDefault().post(new Object(), "sign_out");
                        }
                    }, new DialogUtils.Action() {
                        @Override
                        public void doAction() {

                        }
                    });
                    break;
            }
        }

        public void getOption(final Context context) {

            String url = ConfigUtils.getBaseUrl() + "/api/v1/hems/satisfactionAssess/current";
            Request request = new JSONObjectRequestWithSessionCheck(url,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                // 1 表示可用 0 表示不可用 2 表示已经填写
                                int result = response.getInt("result");

                                if (result == 0){
                                    return;
                                }

                                if (result == 2){
                                    ToastUtils.show("本月满意度调查已完成");
                                    return;
                                }



                                JSONObject assess = response.getJSONObject("assess");
                                String assessId = assess.getString("objectId");
                                TinyDB.getInstance().putString(SfUtils.SF_ASSESS_ID, assessId);

                                JSONArray contents = assess.getJSONArray("contents");
                                List<SatisfactionOption> optionList = new ArrayList<>();
                                int orderNo = 0;
                                for (int i = 0; i != contents.length(); i++) {
                                    JSONObject serviceObject = contents.getJSONObject(i);
                                    String serviceName = (i + 1) + ". " + serviceObject.getString("name");
                                    JSONArray items = serviceObject.getJSONArray("items");
                                    for (int j = 0; j != items.length(); j++) {
                                        SatisfactionOption option = new SatisfactionOption();
                                        option.setTitle(serviceName);
                                        option.setOrderNo(orderNo++);
                                        option.setScore(-1);
                                        option.setNumerator(j + 1);
                                        option.setDenominator(items.length());

                                        JSONObject itemObject = items.getJSONObject(j);
                                        String objectId = itemObject.getString("objectId");
                                        String content = (i + 1) + "." + (j + 1) + " " + itemObject.getString("name");
                                        option.setObjectId(objectId);
                                        option.setContent(content);
                                        optionList.add(option);
                                    }
                                }
                                SimpleApplication.getDaoSession().getSatisfactionOptionDao().deleteAll();
                                SimpleApplication.getDaoSession().getSatisfactionOptionDao().insertInTx(optionList);

                               Intent intent = new Intent(context, SatisfactionActivity.class);
                                context.startActivity(intent);

                            } catch (Exception e) {
//                            ToastUtils.show(e.getMessage());
                            }
                        }
                    },
                    SimpleVolley.getDefaultErrorListener()
            );
            SimpleVolley.addRequest(request);
        }

    }


    // ================================== onCreateViewHolder ==================================

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_main, viewGroup, false));
    }


    // ================================== onBindViewHolder ==================================

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.tvText.setText(texts[position]);
        viewHolder.iivIcon.setIcon(icons[position]);
        if (position < 4) {
            viewHolder.iivIcon.setColorRes(colors[position]);
            viewHolder.iivIcon.setContourColorRes(contourColors[position]);
        } else {
            viewHolder.iivIcon.setColorRes(R.color.md_grey_300);
            viewHolder.iivIcon.setContourColorRes(R.color.md_grey_400);
        }
        viewHolder.iivIcon.setContourWidthDp(1);
    }

    @Override
    public int getItemCount() {
        return texts.length;
    }


}
