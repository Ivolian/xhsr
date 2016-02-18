package unicorn.com.xhsr.groupselect;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.flyco.animation.BounceEnter.BounceTopEnter;
import com.flyco.animation.SlideExit.SlideBottomExit;
import com.flyco.dialog.widget.NormalDialog;
import com.zhy.android.percent.support.PercentFrameLayout;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import unicorn.com.xhsr.R;
import unicorn.com.xhsr.select.SelectObject;

public class SubAdapter extends RecyclerView.Adapter<SubAdapter.ViewHolder> {

    int positionSelected = -1;

    List<SelectObject> dataList = new ArrayList<>();

    public void refreshDataList(List<SelectObject> dataList){
        positionSelected = -1;
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    public void setDataList(List<SelectObject> dataList) {
        this.dataList = dataList;
    }

    public void selectItem(int position) {
        positionSelected = position;
        notifyDataSetChanged();
        SelectObject data = dataList.get(position);
        EventBus.getDefault().post(data.objectId, "onSubSelect");
    }



    public void selectItem(String subId) {
        int position = -1;
        for (SelectObject selectObject:dataList){
            if (subId.equals(selectObject.objectId)){
                position = dataList.indexOf(selectObject);
            }
        }
        selectItem(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.row)
        PercentFrameLayout row;

        @Bind(R.id.value)
        TextView tvValue;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.value)
        public void rowOnClick() {
            selectItem(getAdapterPosition());
        }

        @OnLongClick(R.id.value)

        public boolean rowOnLongClick(){

            Context context = tvValue.getContext();
            int colorPrimary = ContextCompat.getColor(context,R.color.colorPrimary);
            final NormalDialog dialog = new NormalDialog(context);
            dialog.isTitleShow(true)
                    .title("提示")
                    .titleLineColor(colorPrimary)
                    .titleTextColor(colorPrimary)
                    .content("确定将 \"" + tvValue.getText() + "\" 设置为常用?")
                    .contentTextSize(18)
                    .btnText("确认", "取消")//
                    .showAnim(new BounceTopEnter())//
                    .dismissAnim(new SlideBottomExit())//
                    .show();

        return true;
        }
    }


    // ================================== onBindViewHolder ==================================

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        SelectObject data = dataList.get(position);
        viewHolder.tvValue.setText(data.value);

        boolean isSelected = position == positionSelected;
        Context context = viewHolder.tvValue.getContext();
        int textColor = ContextCompat.getColor(context, isSelected ? R.color.md_white : R.color.md_black);
        viewHolder.tvValue.setTextColor(textColor);
        int bgColor = ContextCompat.getColor(context, isSelected ? R.color.colorPrimary : R.color.md_white);
        viewHolder.row.setBackgroundColor(bgColor);
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_group_sub, viewGroup, false));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

}
