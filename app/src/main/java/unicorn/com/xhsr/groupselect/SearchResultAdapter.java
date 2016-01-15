package unicorn.com.xhsr.groupselect;

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

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {

    List<String> valueList ;

    public SearchResultAdapter() {
        valueList = new ArrayList<>();
        for (int i = 0; i != 10; i++) {
            valueList.add("可选值 " + (i + 1));
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.value)
        TextView tvValue;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.row)
        public void rowOnClick() {
            String value = valueList.get(getAdapterPosition());
            EventBus.getDefault().post(value, "onSearchResultSelect");
        }
    }


    // ================================== onBindViewHolder ==================================

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        String value = valueList.get(position);
        viewHolder.tvValue.setText(value);

    }


    //


    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_search_result, viewGroup, false));
    }

    @Override
    public int getItemCount() {
        return valueList.size();
    }

}
