package unicorn.com.xhsr.groupselect.equipment;


import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tonicartos.superslim.GridSLM;
import com.tonicartos.superslim.LinearSLM;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import unicorn.com.xhsr.R;
import unicorn.com.xhsr.goodwork.fastscroll.FastScrollRecyclerViewInterface;
import unicorn.com.xhsr.select.SelectObject;


public class EquipmentAdapter extends RecyclerView.Adapter<EquipmentAdapter.ViewHolder> implements FastScrollRecyclerViewInterface {

    private static final int VIEW_TYPE_HEADER = 0x01;

    private static final int VIEW_TYPE_CONTENT = 0x00;

    private final List<LineItem> mItems;


    int positionSelected = -1;


    public void selectItem(int position) {
        positionSelected = position;
        notifyDataSetChanged();
        EventBus.getDefault().post(mItems.get(position).objectId, "onMainSelect");
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @Nullable
        @Bind(R.id.text)
        TextView text;


        @Nullable
        @Bind(R.id.highlight)
        View highlight;

        @Nullable
        @Bind(R.id.value1)
        TextView tvValue1;


        @Nullable
        @Bind(R.id.textContainer)
        LinearLayout textContainer;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }


        @Nullable
        @OnClick({R.id.value1})
        public void rowOnClick() {
            selectItem(getAdapterPosition());
        }
    }


    // ================================== onBindViewHolder ==================================

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        final LineItem item = mItems.get(position);
        final View itemView = viewHolder.itemView;

        final GridSLM.LayoutParams lp = GridSLM.LayoutParams.from(itemView.getLayoutParams());
        if (item.isHeader) {
            lp.headerEndMarginIsAuto = true;
            lp.headerStartMarginIsAuto = true;
        }
        lp.setSlm(LinearSLM.ID);
        lp.setFirstPosition(item.sectionFirstPosition);
        itemView.setLayoutParams(lp);

        if (!mItems.get(position).isHeader) {


            // 选择设备的特殊处理
            String value = mItems.get(position).text;
          viewHolder.tvValue1.setText(value);

            boolean isSelected = position == positionSelected;
            Context context = viewHolder.tvValue1.getContext();
            int highlightColor = ContextCompat.getColor(context, isSelected ? R.color.colorPrimary : R.color.md_grey_200);
            viewHolder.highlight.setBackgroundColor(highlightColor);
            int textBgColor = ContextCompat.getColor(context, isSelected ? R.color.md_white : R.color.md_grey_200);
            viewHolder.textContainer.setBackgroundColor(textBgColor);
        } else {
            viewHolder.text.setText(mItems.get(position).text);
        }
    }


    @Override
    public int getItemViewType(int position) {
        return mItems.get(position).isHeader ? VIEW_TYPE_HEADER : VIEW_TYPE_CONTENT;
    }


    @Override
    public int getItemCount() {
        return mItems.size();
    }

    //

    private LinkedHashMap<String, Integer> mMapIndex;

    public EquipmentAdapter(List<SelectObject> dataList) {
this.mMapIndex = new LinkedHashMap<>();
        mItems = new ArrayList<>();

        //Insert headers into list of items.
        String lastHeader = "";
        int headerCount = 0;
        int sectionFirstPosition = 0;
        for (int i = 0; i < dataList.size(); i++) {
//            String header = dataList.get(i).value.substring(0, 1);
            String[] arr = dataList.get(i).value.split("/");
            String header = arr[0];
            String text = arr[1];

             String index = header.substring(0,1);
            if (!mMapIndex.containsKey(index)) {
                mMapIndex.put(index, i+headerCount);
            }

            if (!TextUtils.equals(lastHeader, header)) {
                // Insert new header view and update section data.
                sectionFirstPosition = i + headerCount;
                lastHeader = header;
                headerCount += 1;
                mItems.add(new LineItem(dataList.get(i).objectId, header, true, sectionFirstPosition));

            }
            mItems.add(new LineItem(dataList.get(i).objectId, text, false, sectionFirstPosition));
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_HEADER) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.header_item, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_equipment, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public LinkedHashMap<String, Integer> getMapIndex() {
        return this.mMapIndex;
    }


    private static class LineItem {

        public String objectId;

        public int sectionFirstPosition;

        public boolean isHeader;

        public String text;

        public LineItem(String objectId, String text, boolean isHeader,
                        int sectionFirstPosition) {
            this.isHeader = isHeader;
            this.objectId = objectId;
            this.text = text;
            this.sectionFirstPosition = sectionFirstPosition;
        }
    }
}
