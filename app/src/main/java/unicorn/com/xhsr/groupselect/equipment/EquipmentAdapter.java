package unicorn.com.xhsr.groupselect.equipment;


import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import unicorn.com.xhsr.R;
import unicorn.com.xhsr.goodwork.fastscroll.FastScrollRecyclerViewInterface;
import unicorn.com.xhsr.select.SelectObject;


public class EquipmentAdapter extends RecyclerView.Adapter<EquipmentAdapter.ViewHolder> implements FastScrollRecyclerViewInterface {

    int positionSelected = -1;

    List<SelectObject> dataList = new ArrayList<>();

    public void setDataList(List<SelectObject> dataList) {
        this.dataList = dataList;
    }

    public void selectItem(int position) {
        positionSelected = position;
        notifyDataSetChanged();
        SelectObject data = dataList.get(position);
        EventBus.getDefault().post(data.objectId, "onMainSelect");
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.highlight)
        View highlight;

        @Bind(R.id.value1)
        TextView tvValue1;

        @Bind(R.id.value2)
        TextView tvValue2;

        @Bind(R.id.textContainer)
        LinearLayout textContainer;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.row)
        public void rowOnClick() {
            selectItem(getAdapterPosition());
        }
    }


    // ================================== onBindViewHolder ==================================

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        SelectObject data = dataList.get(position);

        // 选择设备的特殊处理
        String value = data.value;
        String[] arr = value.split("/");
        if (arr.length == 2) {
            viewHolder.tvValue1.setText(arr[0]);
            viewHolder.tvValue2.setText(arr[1]);
        }

        boolean isSelected = position == positionSelected;
        Context context = viewHolder.tvValue1.getContext();
        int highlightColor = ContextCompat.getColor(context, isSelected ? R.color.colorPrimary : R.color.md_grey_200);
        viewHolder.highlight.setBackgroundColor(highlightColor);
        int textBgColor = ContextCompat.getColor(context, isSelected ? R.color.md_white : R.color.md_grey_200);
        viewHolder.textContainer.setBackgroundColor(textBgColor);
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_equipment, viewGroup, false));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    //

    private HashMap<String, Integer> mMapIndex;

    public EquipmentAdapter(HashMap<String, Integer> mMapIndex) {
        this.mMapIndex = mMapIndex;
    }

    @Override
    public HashMap<String, Integer> getMapIndex() {
        return this.mMapIndex;
    }

}
