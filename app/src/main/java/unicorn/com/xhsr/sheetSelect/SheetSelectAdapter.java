package unicorn.com.xhsr.sheetSelect;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.simple.eventbus.EventBus;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import unicorn.com.xhsr.R;
import unicorn.com.xhsr.sheetSelect.model.SelectObject;

public class SheetSelectAdapter extends RecyclerView.Adapter<SheetSelectAdapter.ViewHolder> {

    //

    private List<SelectObject> dataList;

    int positionSelected;

    String callbackTag;

    public SheetSelectAdapter(List<SelectObject> dataList, int positionSelected, String callbackTag) {
        this.dataList = dataList;
        this.callbackTag = callbackTag;
        this.positionSelected = positionSelected;
    }

    //

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.value)
        TextView tvValue;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.item)
        public void selectItem() {
            String idSelected = dataList.get(getAdapterPosition()).objectId;
            EventBus.getDefault().post(idSelected, callbackTag);
        }
    }


    // ================================== onBindViewHolder ==================================

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        SelectObject selectObject = dataList.get(position);
        viewHolder.tvValue.setText(selectObject.value);

        // 选中项高亮
        Context context = viewHolder.tvValue.getContext();
        int valueColor = ContextCompat.getColor(context, position == positionSelected ? R.color.colorPrimary : R.color.md_grey_500);
        viewHolder.tvValue.setTextColor(valueColor);
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_select, viewGroup, false));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


}
