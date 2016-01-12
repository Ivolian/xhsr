package unicorn.com.xhsr;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mikepenz.iconics.view.IconicsImageView;
import com.nineoldandroids.view.ViewHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    public static String[] texts = {
            "工单管理", "合同管理", "楼层平面",
            "设备查询", "故障统计", "在线客服"
    };

    public static String[] icons = {
            "cmd-google-glass", "cmd-currency-usd", "cmd-map"
            , "cmd-map-marker-radius", "cmd-link-variant-off", "cmd-headphones-settings"
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

            ViewHelper.setRotation(iivIcon,-30);
        }
    }


    // ================================== onCreateViewHolder ==================================

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_main, viewGroup, false));
    }


    // ================================== onBindViewHolder ==================================

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Model model = modelList.get(position);
        viewHolder.tvText.setText(model.text);
        viewHolder.iivIcon.setIcon(model.icon);
    }


    // ================================== getItemCount ==================================

    @Override
    public int getItemCount() {
        return modelList.size();
    }

}
