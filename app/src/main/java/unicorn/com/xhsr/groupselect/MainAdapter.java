package unicorn.com.xhsr.groupselect;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import unicorn.com.xhsr.R;
import unicorn.com.xhsr.select.SelectObject;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    public int level = -1;

    int positionSelected = -1;

    List<SelectObject> dataList = new ArrayList<>();

    public void setDataList(List<SelectObject> dataList) {
        this.dataList = dataList;
    }

    public void selectItem(int position){
        positionSelected = position;
        SelectObject data = dataList.get(position);
        GroupSelectObject groupSelectObject = GroupSelectHelper.createGroupSelectObject(level,position,data.value);
        EventBus.getDefault().post(groupSelectObject, "onMainSelect");
        notifyDataSetChanged();
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
        viewHolder.tvValue.setText(data.value);

        boolean isSelect = position == positionSelected;
        Context context = viewHolder.tvValue.getContext();
        int highlightColor = ContextCompat.getColor(context, isSelect ? R.color.colorPrimary : R.color.md_grey_200);
        viewHolder.highlight.setBackgroundColor(highlightColor);
        int textBgColor = ContextCompat.getColor(context, isSelect ? R.color.md_white : R.color.md_grey_200);
        viewHolder.tvValue.setBackgroundColor(textBgColor);
    }


    // ==================================  ==================================

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_group_main, viewGroup, false));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

}
