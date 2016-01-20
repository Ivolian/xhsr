package unicorn.com.xhsr;

import android.app.Activity;

import com.f2prateek.dart.Dart;


import butterknife.ButterKnife;

public class ActivityHelp {

    public static void initActivity(Activity activity) {
        ButterKnife.bind(activity);
        Dart.inject(activity);
    }

}
