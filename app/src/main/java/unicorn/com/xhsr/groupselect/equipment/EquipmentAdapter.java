package unicorn.com.xhsr.groupselect.equipment;


import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


    // ====================== view type ======================

    private static final int VIEW_TYPE_HEADER = 0x01;

    private static final int VIEW_TYPE_CONTENT = 0x00;

    @Override
    public int getItemViewType(int position) {
        return mItems.get(position).isHeader ? VIEW_TYPE_HEADER : VIEW_TYPE_CONTENT;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType == VIEW_TYPE_HEADER ?
                R.layout.item_equipment_header : R.layout.item_equipment_content, parent, false);
        return new ViewHolder(view);
    }


    // ====================== selectItem ======================

    int positionSelected = -1;

    public void selectItem(int position) {
        positionSelected = position;
        notifyDataSetChanged();
        EventBus.getDefault().post(mItems.get(position).objectId, "onMainSelect");
    }

    public int getPositionByMainId(String mainId) {
        for (LineItem item : mItems) {
            if (!item.isHeader && item.objectId.equals(mainId)) {
                return mItems.indexOf(item);
            }
        }
        return -1;
    }

    public int getHeaderPositionByMainId(String mainId) {
        int headerPosition = -1;
        for (LineItem item : mItems) {
            if (item.isHeader) {
                headerPosition = mItems.indexOf(item);
            }
            if (!item.isHeader && item.objectId.equals(mainId)) {
                return headerPosition;
            }
        }
        return headerPosition;
    }


    // ====================== ViewHolder ======================

    public class ViewHolder extends RecyclerView.ViewHolder {


        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        // for header

        @Nullable
        @Bind(R.id.header_text)
        TextView tvHeaderText;

        // for content

        @Nullable
        @Bind(R.id.highlight)
        View highlight;

        @Nullable
        @Bind(R.id.content_text)
        TextView tvContentText;

        @Nullable
        @Bind(R.id.padding)
        View padding;

        @Nullable
        @OnClick(R.id.content_text)
        public void rowOnClick() {
                                                                                                                                                                                                        selectItem(getAdapterPosition());
        }
    }


    // ================================== onBindViewHolder ==================================

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        // 讲道理 这段代码我看不懂
        final LineItem item = mItems.get(position);
        final View itemView = holder.itemView;
        final GridSLM.LayoutParams lp = GridSLM.LayoutParams.from(itemView.getLayoutParams());
        if (item.isHeader) {
            lp.headerEndMarginIsAuto = true;
            lp.headerStartMarginIsAuto = true;
        }
        lp.setSlm(LinearSLM.ID);
        lp.setFirstPosition(item.sectionFirstPosition);
        itemView.setLayoutParams(lp);

        // for header
        if (item.isHeader && holder.tvHeaderText != null) {
            holder.tvHeaderText.setText(item.text);
        }

        // for content
        if (!item.isHeader && holder.highlight != null && holder.tvContentText != null && holder.padding != null) {
            holder.tvContentText.setText(item.text);

            boolean isSelected = position == positionSelected;
            Context context = holder.tvContentText.getContext();

            int highlightColor = ContextCompat.getColor(context, isSelected ? R.color.colorPrimary : R.color.md_grey_200);
            holder.highlight.setBackgroundColor(highlightColor);

            int textBgColor = ContextCompat.getColor(context, isSelected ? R.color.md_white : R.color.md_grey_200);
            holder.tvContentText.setBackgroundColor(textBgColor);
            holder.padding.setBackgroundColor(textBgColor);
        }
    }


    // ================================== constructor ==================================

    private List<LineItem> mItems;

    private LinkedHashMap<String, Integer> mMapIndex;

    public EquipmentAdapter(List<SelectObject> dataList) {
        mItems = new ArrayList<>();
        mMapIndex = new LinkedHashMap<>();

        String lastHeaderText = "";
        int headerCount = 0;
        int sectionFirstPosition = 0;
        for (int i = 0; i < dataList.size(); i++) {
            String[] texts = dataList.get(i).value.split("/");
            String headerText = texts[0];
            String contentText = texts[1];
            if (!TextUtils.equals(lastHeaderText, headerText)) {
                sectionFirstPosition = i + headerCount;
                mItems.add(new LineItem("", headerText, true, sectionFirstPosition));
                mMapIndex.put(headerText.substring(0, 1), sectionFirstPosition);
                lastHeaderText = headerText;
                headerCount++;
            }
            mItems.add(new LineItem(dataList.get(i).objectId, contentText, false, sectionFirstPosition));
        }
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

        public LineItem(String objectId, String text, boolean isHeader, int sectionFirstPosition) {
            this.isHeader = isHeader;
            this.objectId = objectId;
            this.text = text;
            this.sectionFirstPosition = sectionFirstPosition;
        }
    }


    @Override
    public int getItemCount() {
        return mItems.size();
    }

}
