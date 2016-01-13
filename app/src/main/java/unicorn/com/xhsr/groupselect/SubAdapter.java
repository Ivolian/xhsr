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
import unicorn.com.xhsr.R;

public class SubAdapter extends RecyclerView.Adapter<SubAdapter.ViewHolder> {

    public int level = -1;

    int positionSelected = -1;

    List<String> valueList = new ArrayList<>();

    public void setValueList(List<String> valueList) {
        this.valueList = valueList;
    }

    public void selectItem(int position) {
        positionSelected = position;
        String value = valueList.get(position);
        GroupSelectObject groupSelectObject = GroupSelectHelper.create(level, position, value);
        EventBus.getDefault().post(groupSelectObject, "onSubSelect");
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        @Bind(R.id.row)
        PercentFrameLayout  row;
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
    }


    // ================================== onBindViewHolder ==================================

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        String value = valueList.get(position);
        viewHolder.tvValue.setText(value);

        if (level == 3) {

            boolean isSelect = position == positionSelected;
            Context context = viewHolder.tvValue.getContext();

            int textColor = ContextCompat.getColor(context, isSelect ? R.color.md_white : R.color.md_black);
            viewHolder.tvValue.setTextColor(textColor);

            int bgColor = ContextCompat.getColor(context, isSelect ? R.color.md_blue_grey_500 : R.color.md_white);
            viewHolder.row.setBackgroundColor(bgColor);


        }else {
            Context context = viewHolder.tvValue.getContext();
            int textColor = ContextCompat.getColor( context,R.color.md_black);
            viewHolder.tvValue.setTextColor(textColor);

            int bgColor = ContextCompat.getColor(context, R.color.md_white);
            viewHolder.row.setBackgroundColor(bgColor);

        }
    }


    //


    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_group_sub, viewGroup, false));
    }

    @Override
    public int getItemCount() {
        return valueList.size();
    }

}
