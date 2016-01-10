package unicorn.com.xhsr;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.amulyakhare.textdrawable.TextDrawable;
import com.bartoszlipinski.recyclerviewheader.RecyclerViewHeader;

import butterknife.ButterKnife;
import unicorn.com.xhsr.draglayout.view.DragLayout;

public class MainActivity extends AppCompatActivity {

    private DragLayout dl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setContentView(R.layout.activity_main);
        initDragLayout();


//        findViewById(R.id.setting).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                ToastUtils.show("hehe");
//                dl.open(true);
//
//            }
//        });




        initRecyclerView();
    }


    private void initDragLayout() {
        dl = (DragLayout) findViewById(R.id.dl);
        dl.setDragListener(new DragLayout.DragListener() {
            @Override
            public void onOpen() {

            }

            @Override
            public void onClose() {

            }

            @Override
            public void onDrag(float percent) {
//            ToastUtils.show(percent);
            }
        });
    }


    private void initRecyclerView() {
        RecyclerView mWaterFallRcv = (RecyclerView) findViewById(R.id.recycleview);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mWaterFallRcv.setLayoutManager(layoutManager);

        // 添加分割线
        mWaterFallRcv.addItemDecoration(new DividerGridItemDecoration(this));




          mWaterFallRcv.setAdapter(new MainAdapter());


        RecyclerViewHeader header = RecyclerViewHeader.fromXml(this, R.layout.recycle_view_head_view);

        LinearLayout linearLayout = (LinearLayout)header.findViewById(R.id.test);
        TextDrawable drawable = TextDrawable.builder()
                .buildRound("", getResources().getColor(R.color.md_blue_400));
        linearLayout.setBackground(drawable);

        linearLayout = (LinearLayout)header.findViewById(R.id.test2);
        drawable = TextDrawable.builder()
                .buildRound("", getResources().getColor(R.color.md_red_400));
        linearLayout.setBackground(drawable);

        linearLayout = (LinearLayout)header.findViewById(R.id.test3);
        drawable = TextDrawable.builder()
                .buildRound("", getResources().getColor(R.color.md_teal_400));
        linearLayout.setBackground(drawable);
        header.attachTo(mWaterFallRcv);






    }


}
