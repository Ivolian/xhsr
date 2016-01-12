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

public class SelectAdapter extends RecyclerView.Adapter<SelectAdapter.ViewHolder> {


    private List<String> dataList;
        String eventTag;

  SelectObject selectObject;

    public SelectAdapter(String eventTag, SelectObject selectObject) {

        this.eventTag = eventTag;
        this.selectObject = selectObject;
        dataList = new ArrayList<>();
        for (int i = 0; i !=30; i++) {
            dataList.add("故障 " + i);
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.text)
        TextView tvText;


        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.text)
        public void test() {
            ToastUtils.show("hehe");
            String value = dataList.get(getAdapterPosition());

            SelectObject selectObject = new SelectObject();
            selectObject.value = value;
            selectObject.position = getAdapterPosition();
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
        String value = dataList.get(position);
        viewHolder.tvText.setText(value);

        if (selectObject!=null) {
            if (position == selectObject.position) {
                viewHolder.tvText.setTextColor(ContextCompat.getColor(viewHolder.tvText.getContext(), R.color.colorPrimary));
            }else {
                viewHolder.tvText.setTextColor(ContextCompat.getColor(viewHolder.tvText.getContext(), R.color.md_grey_500));

            }
        }

    }


    // ================================== getItemCount ==================================

    @Override
    public int getItemCount() {
        return dataList.size();
    }

}
