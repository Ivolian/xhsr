package unicorn.com.xhsr.groupselect;


import android.app.Activity;
import android.content.Intent;

public class GroupSelectHelper {


    public static void startGroupSelectActivity(Activity activity, GroupSelectActivity.DataProvider dataProvider, String name, String subId, int resultCode) {
        GroupSelectActivity.setDataProvider(dataProvider);
        Intent intent = new Intent(activity, GroupSelectActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("subId", subId);
        intent.putExtra("resultCode", resultCode);
        activity.startActivityForResult(intent, 2333);
    }

}
