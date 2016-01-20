package unicorn.com.xhsr.select;

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

public class SelectAdapter extends RecyclerView.Adapter<SelectAdapter.ViewHolder> {

    public interface DataProvider {
        List<SelectObject> getDataList();
    }

    //

    DataProvider dataProvider;

    int positionSelected;

    String callbackTag;

    public SelectAdapter(DataProvider dataProvider, int positionSelected, String eventTag) {
        this.dataProvider = dataProvider;
        this.callbackTag = eventTag;
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

        @OnClick(R.id.row)
        public void selectItem() {
            String objectIdSelected = dataProvider.getDataList().get(getAdapterPosition()).objectId;
            EventBus.getDefault().post(objectIdSelected, callbackTag);
        }
    }


    // ================================== onBindViewHolder ==================================

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        SelectObject selectObject = dataProvider.getDataList().get(position);
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
        return dataProvider.getDataList().size();
    }

}
