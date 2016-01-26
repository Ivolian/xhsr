package unicorn.com.xhsr.myorder;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.amulyakhare.textdrawable.TextDrawable;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import unicorn.com.xhsr.R;
import unicorn.com.xhsr.orderdetail.OrderDetailActivity;


public class ToRepairAdapter extends RecyclerView.Adapter<ToRepairAdapter.ViewHolder> {


    private List<Object> dataList = new ArrayList<>();


    public ToRepairAdapter() {
        for (int i = 0; i != 20; i++) {
            dataList.add(new Object());
        }
    }


    // ================================== viewHolder ==================================

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.textDrawable)
        ImageView ivTextDrawable;


        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }

        @OnClick(R.id.row)
        public void rowOnClick() {
            Context context = ivTextDrawable.getContext();
            Intent intent = new Intent(context, OrderDetailActivity.class);
            context.startActivity(intent);
        }


    }


    // ================================== onCreateViewHolder ==================================

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_wait_repair, viewGroup, false));
    }


    // ================================== onBindViewHolder ==================================

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        int colorPrimary = ContextCompat.getColor(viewHolder.ivTextDrawable.getContext(), R.color.colorPrimary);
        TextDrawable textDrawable = TextDrawable.builder().buildRound("修", colorPrimary);
        viewHolder.ivTextDrawable.setImageDrawable(textDrawable);
    }


    // ================================== getItemCount ==================================

    @Override
    public int getItemCount() {
        return dataList.size();
    }

}
