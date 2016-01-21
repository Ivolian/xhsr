package unicorn.com.xhsr.groupselect;


import android.app.Activity;
import android.content.Intent;

public class GroupSelectHelper {



    public static void startGroupSelectActivity(Activity activity, String name,int resultCode) {
        Intent intent = new Intent(activity, GroupSelectActivity.class);
        intent.putExtra("name", name);
//        intent.putExtra("mainId", mainId);
//        intent.putExtra("subId", subId);
        intent.putExtra("resultCode", resultCode);
        activity.startActivityForResult(intent, 2333);
    }

}
