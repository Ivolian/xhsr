package unicorn.com.xhsr;

import android.app.Activity;

import com.f2prateek.dart.Dart;
import com.github.ppamorim.dragger.DraggerActivity;
import com.github.ppamorim.dragger.DraggerPosition;

import butterknife.ButterKnife;

public class ActivityHelp {

    public static void initActivity(Activity activity) {
        ButterKnife.bind(activity);
        Dart.inject(activity);
        if (activity instanceof DraggerActivity) {
            DraggerActivity draggerActivity = (DraggerActivity) activity;
            draggerActivity.setSlideEnabled(false);
            draggerActivity.setTension(2);
            draggerActivity.setDraggerPosition(DraggerPosition.RIGHT);
        }
    }

}
