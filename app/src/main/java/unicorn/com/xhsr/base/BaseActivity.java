package unicorn.com.xhsr.base;

import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;

import com.f2prateek.dart.Dart;

import org.simple.eventbus.EventBus;

import butterknife.ButterKnife;


public class BaseActivity extends AppCompatActivity {


    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        EventBus.getDefault().register(this);
        Dart.inject(this);
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }

}
