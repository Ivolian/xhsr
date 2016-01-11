package unicorn.com.xhsr;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;


public class QuickOrderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_order);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews(){

    }


}
