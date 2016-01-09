package unicorn.com.xhsr;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.amulyakhare.textdrawable.TextDrawable;

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

        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.test);
        TextDrawable drawable = TextDrawable.builder()
                .buildRound("", getResources().getColor(R.color.md_blue_400));
        linearLayout.setBackground(drawable);

         linearLayout = (LinearLayout)findViewById(R.id.test2);
         drawable = TextDrawable.builder()
                .buildRound("", getResources().getColor(R.color.md_red_400));
        linearLayout.setBackground(drawable);

        linearLayout = (LinearLayout)findViewById(R.id.test3);
        drawable = TextDrawable.builder()
                .buildRound("", getResources().getColor(R.color.md_teal_400));
        linearLayout.setBackground(drawable);



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

}
