package unicorn.com.xhsr.groupselect;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhy.android.percent.support.PercentFrameLayout;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import unicorn.com.xhsr.R;
import unicorn.com.xhsr.SimpleApplication;
import unicorn.com.xhsr.data.greendao.Equipment;
import unicorn.com.xhsr.select.SelectObject;

public class SubAdapter extends RecyclerView.Adapter<SubAdapter.ViewHolder> {

    int positionSelected = -1;

    List<SelectObject> dataList = new ArrayList<>();

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

            final SelectObject data = dataList.get(getAdapterPosition());
            new SweetAlertDialog(tvValue.getContext(), SweetAlertDialog.NORMAL_TYPE)
                    .setTitleText("设为常用？")
                    .setConfirmText("确认")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            Equipment equipment = new Equipment();
                            equipment.setOrderNo(0);
                            equipment.setCategoryId("root");
                            equipment.setName(data.value);
                            equipment.setObjectId(data.objectId);

                            SimpleApplication.getDaoSession().getEquipmentDao().insert(equipment);

                            sDialog.dismissWithAnimation();
                        }
                    })
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
