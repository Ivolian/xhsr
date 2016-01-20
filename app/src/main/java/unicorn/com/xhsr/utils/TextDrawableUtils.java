package unicorn.com.xhsr.utils;


import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;

import com.amulyakhare.textdrawable.TextDrawable;

public class TextDrawableUtils {

    public static TextDrawable getCircleDrawable(Context context,@ColorRes int colorRes) {
        int color = ContextCompat.getColor(context, colorRes);
        return TextDrawable.builder().buildRound("", color);
    }

}
