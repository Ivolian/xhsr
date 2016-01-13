package unicorn.com.xhsr;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class GroupMainAdapter extends RecyclerView.Adapter<GroupMainAdapter.ViewHolder> {

    List<String> valueList;



    public GroupMainAdapter() {

        valueList = new ArrayList<>();
        for (int i = 0; i != 30; i++) {
            valueList.add("列表值 " + (i + 1));
        }

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


    }


    // ================================== onCreateViewHolder ==================================

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_group_main, viewGroup, false));
    }


    // ================================== onBindViewHolder ==================================

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        String value = valueList.get(position);
        viewHolder.tvValue.setText(value);

        if (position == 2){
                viewHolder.highlight.setBackgroundColor(ContextCompat.getColor(viewHolder.highlight.getContext(),R.color.colorPrimary));
                viewHolder.tvValue.setBackgroundColor(Color.WHITE);
        }
    }


    // ================================== getItemCount ==================================

    @Override
    public int getItemCount() {
        return valueList.size();
    }

}
