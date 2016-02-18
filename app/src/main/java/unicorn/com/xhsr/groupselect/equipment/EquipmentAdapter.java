package unicorn.com.xhsr.groupselect.equipment;

/**
 * Created by Administrator on 2016/2/18.
 */

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    public List<SelectObject> getDataList() {
        return dataList;
    }

    public void setDataList(List<SelectObject> dataList) {
        this.dataList = dataList;
    }

    public void selectItem(int position) {
        positionSelected = position;
        notifyDataSetChanged();
        SelectObject data = dataList.get(position);
        EventBus.getDefault().post(data.objectId, "onMainSelect");
    }


    public void selectItem(String mainId) {
        int position = -1;
        for (SelectObject selectObject:dataList){
            if (mainId.equals(selectObject.objectId)){
                position = dataList.indexOf(selectObject);
            }
        }
        selectItem(position);
    }
    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.highlight)
        View highlight;

        @Bind(R.id.value)
        TextView tvValue;

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
            value = arr[0] + "\r\n" + arr[1];
        }
        viewHolder.tvValue.setText(value);

        boolean isSelected = position == positionSelected;
        Context context = viewHolder.tvValue.getContext();
        int highlightColor = ContextCompat.getColor(context, isSelected ? R.color.colorPrimary : R.color.md_grey_200);
        viewHolder.highlight.setBackgroundColor(highlightColor);
        int textBgColor = ContextCompat.getColor(context, isSelected ? R.color.md_white : R.color.md_grey_200);
        viewHolder.tvValue.setBackgroundColor(textBgColor);
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_group_main, viewGroup, false));
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
