package unicorn.com.xhsr;

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
import unicorn.com.xhsr.greendao.ProcessingMode;
import unicorn.com.xhsr.greendao.ProcessingModeDao;

public class SelectAdapter extends RecyclerView.Adapter<SelectAdapter.ViewHolder> {

    List<Object> dataList;

    String callbackTag;

    int positionSelected;

    public SelectAdapter(String callbackTag, int positionSelected) {
        if (callbackTag.equals("onProcessModeSelect")){
            SimpleApplication.getMenuDao().queryBuilder().orderAsc(ProcessingModeDao.Properties.OrderNo).list();

        }

        dataList = new ArrayList<>();
        for (int i = 0; i != 4; i++) {
            dataList.add("可选值 " + (i + 1));
        }
        this.callbackTag = callbackTag;
        this.positionSelected = positionSelected;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.value)
        TextView tvValue;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.row)
        public void selectItem() {
            int position = getAdapterPosition();
            String value = dataList.get(position);
            SelectObject selectObject = new SelectObject();
            selectObject.position = position;
            selectObject.value = value;
            EventBus.getDefault().post(selectObject, callbackTag);
        }
    }


    // ================================== onBindViewHolder ==================================

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        String value = dataList.get(position);
        viewHolder.tvValue.setText(value);
        int textColor = ContextCompat.getColor(viewHolder.tvValue.getContext(), position == positionSelected ? R.color.colorPrimary : R.color.md_grey_500);
        viewHolder.tvValue.setTextColor(textColor);
    }


    // ==================================  ==================================

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_select, viewGroup, false));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

}
