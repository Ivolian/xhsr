package unicorn.com.xhsr;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mikepenz.iconics.view.IconicsImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import unicorn.com.xhsr.data.Model;
import unicorn.com.xhsr.detailorder.DetailOrderActivity;
import unicorn.com.xhsr.quickorder.QuickOrderActivity;


public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    public static String[] texts = {
            "快速下单", "详细下单", "满意度调查",
            "楼层平面", "故障统计", "联系客服",
            "添加更多", "故障统计", "联系客服"

    };

    public static String[] icons = {
            "gmi-assignment-check", "gmi-assignment", "gmi-flower-alt"
            , "gmi-home", "gmi-wrench", "gmi-whatsapp"
            , "gmi-plus", "gmi-wrench", "gmi-whatsapp"
    };

    private List<Model> modelList;

    public MainAdapter() {
        modelList = new ArrayList<>();
        for (int i = 0; i != texts.length; i++) {
            Model model = new Model();
            model.text = texts[i];
            model.icon = icons[i];
            modelList.add(model);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.value)
        TextView tvText;

        @Bind(R.id.icon)
        IconicsImageView iivIcon;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

//            ViewHelper.setRotation(iivIcon, -30);
        }

        @OnClick(R.id.row)
        public void rowOnClick() {
            Intent intent;
            int position = getAdapterPosition();
            switch (position) {
                case 0:
                    intent = new Intent(tvText.getContext(), QuickOrderActivity.class);
                    tvText.getContext().startActivity(intent);
                    break;
                case 1:
                    intent = new Intent(tvText.getContext(), DetailOrderActivity.class);
                    tvText.getContext().startActivity(intent);
                    break;
            }
        }

    }


    // ================================== onCreateViewHolder ==================================

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_main, viewGroup, false));
    }


    // ================================== onBindViewHolder ==================================

    private int[] colors = {R.color.md_teal_400, R.color.md_brown_400, R.color.md_red_300,
            R.color.md_red_50, R.color.md_red_50, R.color.md_red_50,
            R.color.md_red_50, R.color.md_red_50, R.color.md_red_50};

    private int[] contourColors = {
            R.color.md_teal_500,R.color.md_brown_500,R.color.md_red_400
    };

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Model model = modelList.get(position);


        viewHolder.tvText.setText(model.text);
        viewHolder.iivIcon.setIcon(model.icon);
        viewHolder.iivIcon.setColorRes(position < 3 ? colors[position] : R.color.md_grey_300);
      if (position<3) {
          viewHolder.iivIcon.setContourColorRes(contourColors[position]);

          viewHolder.iivIcon.setContourWidthDp(1);
      }else {
          viewHolder.iivIcon.setContourColorRes(R.color.md_grey_400);
          viewHolder.iivIcon.setContourWidthDp(1);
      }


    }


    // ================================== getItemCount ==================================

    @Override
    public int getItemCount() {
        return modelList.size();
    }

}
