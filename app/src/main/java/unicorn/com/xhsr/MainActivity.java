package unicorn.com.xhsr;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import unicorn.com.xhsr.draglayout.view.DragLayout;

public class MainActivity extends AppCompatActivity {

    private DragLayout dl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initDragLayout();

//        findViewById(R.id.tv).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                ToastUtils.show("模糊");
//                Blurry.with(MainActivity.this)
//                        .radius(25)
//                        .sampling(2)
//                        .color(Color.argb(66, 0, 0, 0))
//                        .async()
//                        .animate(500)
//                        .onto((ViewGroup) findViewById(R.id.dl));
//            }
//        });

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
