package unicorn.com.xhsr;

import android.view.Gravity;
import android.widget.Toast;


public class ToastUtils {

    private static Toast mToast = null;

    public static void show(String text) {

        if (mToast == null) {
            mToast = Toast.makeText(SimpleApplication.getInstance(), text, Toast.LENGTH_SHORT);
            mToast.setGravity(Gravity.CENTER, 0, 0);
        }
        else {
            mToast.setText(text);
        }
        mToast.show();
    }

    public static void show(Object object){
        ToastUtils.show(object.toString());
    }

}
