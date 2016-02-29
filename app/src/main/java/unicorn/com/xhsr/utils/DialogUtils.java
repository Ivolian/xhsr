package unicorn.com.xhsr.utils;


import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.afollestad.materialdialogs.MaterialDialog;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;

import unicorn.com.xhsr.R;

public class DialogUtils {

    public interface Action {
        void doAction();
    }

    public static void showConfirm(Context context, String content, final Action left, final Action right) {
        int color = ContextCompat.getColor(context, R.color.colorPrimary);
        final NormalDialog dialog = new NormalDialog(context);
        dialog.isTitleShow(true)
                .title("提示")
                .titleLineColor(color)
                .titleTextColor(color)
                .content(content)
                .contentTextSize(18)
                .btnText("确认", "取消")
                .show();
        dialog.setOnBtnClickL(
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        left.doAction();
                        dialog.dismiss();
                    }
                },
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        right.doAction();
                        dialog.dismiss();
                    }
                }
        );
    }


    public static MaterialDialog showMask(Context context, String title, String content) {
        return new MaterialDialog.Builder(context)
                .title(title)
                .content(content)
                .progress(true, 0)
                .cancelable(false)
                .show();
    }

    public static MaterialDialog showMask2(Context context, String title, String content) {
        return new MaterialDialog.Builder(context)
                .title(title)
                .content(content)
                .progress(false, 100)
                .cancelable(false)
                .show();
    }


}
