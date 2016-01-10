package unicorn.com.xhsr;

import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.bartoszlipinski.recyclerviewheader.RecyclerViewHeader;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import unicorn.com.xhsr.draglayout.view.DragLayout;

public class MainActivity extends AppCompatActivity {


    // =============================== onCreate ===============================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        initDragLayout();
        initRecyclerView();
    }

    // =============================== drag layout ===============================

    @Bind(R.id.dl)
    DragLayout dragLayout;

    private void initDragLayout() {
        dragLayout.setDragListener(new DragLayout.DragListener() {
            @Override
            public void onOpen() {

            }

            @Override
            public void onClose() {

            }

            @Override
            public void onDrag(float percent) {
            }
        });
    }


    // =============================== recycleview ===============================

    @Bind(R.id.recycleview)
    RecyclerView recyclerView;

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.addItemDecoration(new DividerGridItemDecoration(this));
        recyclerView.setAdapter(new MainAdapter());
        initRecycleViewHeader();
    }

    private void initRecycleViewHeader() {
        RecyclerViewHeader recyclerViewHeader = RecyclerViewHeader.fromXml(this, R.layout.recycle_view_head);
        recyclerViewHeader.findViewById(R.id.test).setBackground(getCircleDrawable(R.color.md_blue_400));
        recyclerViewHeader.findViewById(R.id.test2).setBackground(getCircleDrawable(R.color.md_red_400));
        recyclerViewHeader.findViewById(R.id.test3).setBackground(getCircleDrawable(R.color.md_teal_400));
        recyclerViewHeader.attachTo(recyclerView);
    }

    private TextDrawable getCircleDrawable(@ColorRes int colorRes) {
        int color = ContextCompat.getColor(this, colorRes);
        return TextDrawable.builder().buildRound("", color);
    }


    // =============================== onClick ===============================

    @OnClick(R.id.setting)
    public void onSettingClick() {
        dragLayout.open(true);
    }


}
