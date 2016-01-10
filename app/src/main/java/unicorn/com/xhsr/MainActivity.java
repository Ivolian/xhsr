package unicorn.com.xhsr;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.amulyakhare.textdrawable.TextDrawable;
import com.bartoszlipinski.recyclerviewheader.RecyclerViewHeader;
import com.karumi.dividers.DividerBuilder;
import com.karumi.dividers.DividerItemDecoration;
import com.karumi.dividers.Layer;

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


        findViewById(R.id.setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ToastUtils.show("hehe");
                dl.open(true);

            }
        });




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

    MainAdapter mainAdapter;


    public  GridLayoutManager getStaggeredGridLayoutManager(){
       GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        return  gridLayoutManager;
    }

    public  LinearLayoutManager getLinearLayoutManager(Context context) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        return linearLayoutManager;
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recycleview);
        recyclerView.setLayoutManager(getStaggeredGridLayoutManager());
        mainAdapter = new MainAdapter();
        recyclerView.setAdapter(mainAdapter);
//        recyclerView.addItemDecoration(new DividerGridItemDecoration(this));

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
        header.attachTo(recyclerView);

        // Create a drawable for your divider
        Drawable exampleDrawable = getResources().getDrawable(android.R.drawable.divider_horizontal_dark);

// Create a DividerItemDecoration instance with a single layer and add it to your recycler view
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(new Layer(DividerBuilder.get().with(exampleDrawable).build()));
        recyclerView.addItemDecoration(itemDecoration);

    }


}
