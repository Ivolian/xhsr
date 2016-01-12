package unicorn.com.xhsr;

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

public class SelectAdapter extends RecyclerView.Adapter<SelectAdapter.ViewHolder> {

    List<String> valueList;

    String eventTag;

    SelectObject selectObject;

    public SelectAdapter(String eventTag, SelectObject selectObject) {

        valueList = new ArrayList<>();
        for (int i = 0; i != 30; i++) {
            valueList.add("列表值 " + (i + 1));
        }

        this.eventTag = eventTag;
        this.selectObject = selectObject;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.value)
        TextView tvValue;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.row)
        public void selectRow() {
            int position = getAdapterPosition();
            String value = valueList.get(position);
            SelectObject selectObject = new SelectObject();
            selectObject.position = position;
            selectObject.value = value;
            EventBus.getDefault().post(selectObject, eventTag);
        }
    }


    // ================================== onCreateViewHolder ==================================

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_select, viewGroup, false));
    }


    // ================================== onBindViewHolder ==================================

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        String value = valueList.get(position);
        viewHolder.tvValue.setText(value);
        if (selectObject != null) {
            Context context = viewHolder.tvValue.getContext();
            int textColor = ContextCompat.getColor(context, position == selectObject.position ? R.color.colorPrimary : R.color.md_grey_500);
            viewHolder.tvValue.setTextColor(textColor);
        }
    }


    // ================================== getItemCount ==================================

    @Override
    public int getItemCount() {
        return valueList.size();
    }

}
