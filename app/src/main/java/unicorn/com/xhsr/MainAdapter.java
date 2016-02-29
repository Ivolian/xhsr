package unicorn.com.xhsr;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mikepenz.iconics.view.IconicsImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import unicorn.com.xhsr.detailorder.DetailOrderActivity;
import unicorn.com.xhsr.quickorder.QuickOrderActivity;
import unicorn.com.xhsr.satisfaction.SatisfactionActivity;


public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {


    // ================================== values ==================================

    private String[] texts = {"快速下单", "详细下单", "满意度调查", "楼层平面", "故障统计", "联系客服", "添加更多"};

    private String[] icons = {"gmi-assignment-check", "gmi-assignment", "gmi-flower-alt", "gmi-home", "gmi-wrench", "gmi-whatsapp", "gmi-plus"};

    private int[] colors = {R.color.md_teal_400, R.color.md_brown_400, R.color.md_red_300};

    private int[] contourColors = {R.color.md_teal_500, R.color.md_brown_500, R.color.md_red_400};

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
            Class activityClass = null;
            switch (getAdapterPosition()) {
                case 0:
                    activityClass = QuickOrderActivity.class;
                    break;
                case 1:
                    activityClass = DetailOrderActivity.class;
                    break;
                case 2:
                    activityClass = SatisfactionActivity.class;
                    break;
            }
            if (activityClass != null) {
                Context context = tvText.getContext();
                Intent intent = new Intent(context, activityClass);
                tvText.getContext().startActivity(intent);
            }
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
        if (position < 3) {
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
