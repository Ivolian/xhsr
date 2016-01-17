package unicorn.com.xhsr.select;

import android.content.Context;
import android.provider.ContactsContract;
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
import unicorn.com.xhsr.DataHelp;
import unicorn.com.xhsr.R;
import unicorn.com.xhsr.SimpleApplication;
import unicorn.com.xhsr.greendao.ProcessingMode;
import unicorn.com.xhsr.greendao.ProcessingModeDao;

public class SelectAdapter extends RecyclerView.Adapter<SelectAdapter.ViewHolder> {

    List<SelectObject> dataList;

    String callbackTag;

    int positionSelected;

    public SelectAdapter(String callbackTag, int positionSelected) {
        if (callbackTag.equals("onProcessModeSelect")) {
            dataList = new ArrayList<>();
            for (ProcessingMode processingMode : DataHelp.getProcessModeList()) {
                SelectObject selectObject = new SelectObject();
                selectObject.value = processingMode.getName();
                selectObject.objectId = processingMode.getObjectId();
                dataList.add(selectObject);
            }
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
            SelectObject selectObject = dataList.get(position);
            SelectObjectWithPosition selectObjectWithPosition = SelectHelper.create(selectObject, position);
            EventBus.getDefault().post(selectObjectWithPosition, callbackTag);
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
