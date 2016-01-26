package unicorn.com.xhsr.orderdetail;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.devspark.robototextview.widget.RobotoTextView;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import unicorn.com.xhsr.R;
import unicorn.com.xhsr.data.OrderFlowModel;


public class OrderFlowAdapter extends RecyclerView.Adapter<OrderFlowAdapter.ViewHolder> {


    private List<OrderFlowModel> dataList = new ArrayList<>();


    public OrderFlowAdapter() {
       OrderFlowModel orderFlowModel = new OrderFlowModel();
        orderFlowModel.date =  "2016-01-16";
        orderFlowModel.text = "已签收，感谢使用顺丰，期待再次为您服务";
        orderFlowModel.time = "08:41:53";
        dataList.add(orderFlowModel);

         orderFlowModel = new OrderFlowModel();
        orderFlowModel.text = "快件正在送往顺丰店";
        orderFlowModel.time = "08:05:22";
        dataList.add(orderFlowModel);

        orderFlowModel = new OrderFlowModel();
        orderFlowModel.text = "快件到达 【无锡新区贝多工业园营点】";
        orderFlowModel.time = "07:07:09";
        dataList.add(orderFlowModel);

        orderFlowModel = new OrderFlowModel();
        orderFlowModel.text = "快件离开 【无锡硕放集散中心】 ，正发往 【无锡新区贝多工业园营点】";
        orderFlowModel.time = "01:49:57";
        dataList.add(orderFlowModel);

        orderFlowModel = new OrderFlowModel();
        orderFlowModel.text = "快件正在送往顺丰店";
        orderFlowModel.time = "08:05:22";
        dataList.add(orderFlowModel);

        orderFlowModel = new OrderFlowModel();
        orderFlowModel.date =  "2016-01-15";
        orderFlowModel.text = "快件到达 【无锡新区贝多工业园营点】";
        orderFlowModel.time = "07:07:09";
        dataList.add(orderFlowModel);

        orderFlowModel = new OrderFlowModel();
        orderFlowModel.text = "快件离开 【无锡硕放集散中心】 ，正发往 【无锡新区贝多工业园营点】";
        orderFlowModel.time = "01:49:57";
        dataList.add(orderFlowModel);
    }


    // ================================== viewHolder ==================================

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.date)
        RobotoTextView rtvDate;

        @Bind(R.id.circle)
        ImageView circle;

        @Bind(R.id.message)
        TextView rtvMessage;

        @Bind(R.id.time)
        RobotoTextView rtvTime;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.row)
        public void rowOnClick() {

        }

    }


    // ================================== onCreateViewHolder ==================================

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_order_flow, viewGroup, false));
    }


    // ================================== onBindViewHolder ==================================

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Context context = viewHolder.circle.getContext();
        OrderFlowModel orderFlowModel  = dataList.get(position);
        if (orderFlowModel.date ==null){
            int color = ContextCompat.getColor(context,R.color.md_grey_300);
            Drawable drawable = new IconicsDrawable(viewHolder.circle.getContext())
                    .icon(MaterialDesignIconic.Icon.gmi_circle_o)
                    .color(color)
                    .sizeDp(24);
            viewHolder.circle.setImageDrawable(drawable);
            viewHolder.rtvDate.setVisibility(View.INVISIBLE);
        }else {
            viewHolder.rtvDate.setVisibility(View.VISIBLE);
            viewHolder.rtvDate.setText(orderFlowModel.date);
            int color = ContextCompat.getColor(context,R.color.colorAccent);
            Drawable drawable = new IconicsDrawable(viewHolder.circle.getContext())
                    .icon(MaterialDesignIconic.Icon.gmi_dot_circle_alt)
                    .color(color)
                    .sizeDp(24);
            viewHolder.circle.setImageDrawable(drawable);
        }
        viewHolder.rtvMessage.setText(orderFlowModel.text);
        viewHolder.rtvTime.setText(orderFlowModel.time);

    }


    // ================================== getItemCount ==================================

    @Override
    public int getItemCount() {
        return dataList.size();
    }

}
