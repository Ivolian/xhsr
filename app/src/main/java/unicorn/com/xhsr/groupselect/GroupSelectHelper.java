package unicorn.com.xhsr.groupselect;


import android.app.Activity;
import android.content.Intent;

public class GroupSelectHelper {

    public static String RESULT = "result";

    public static void startGroupSelectActivity(Activity activity, String name, String selectedId, int resultCode) {
        Intent intent = new Intent(activity, GroupSelectActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("selectedId", selectedId);
        intent.putExtra("resultCode", resultCode);
        activity.startActivityForResult(intent, 2333);
    }

}
