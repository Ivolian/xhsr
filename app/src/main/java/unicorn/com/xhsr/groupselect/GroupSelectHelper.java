package unicorn.com.xhsr.groupselect;


import android.app.Activity;
import android.content.Intent;

import unicorn.com.xhsr.SelectObject;

public class GroupSelectHelper {

    public static String RESULT = "result";

    public static GroupSelectObject create(int level, int position, String value) {
        SelectObject selectObject = new SelectObject();
        selectObject.position = position;
        selectObject.value = value;
        GroupSelectObject groupSelectObject = new GroupSelectObject();
        groupSelectObject.selectObject = selectObject;
        groupSelectObject.level = level;
        return groupSelectObject;
    }

    public static void startGroupSelectActivity(Activity activity, String title, int maxLevel) {
        Intent intent = new Intent(activity, GroupSelectActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("maxLevel", maxLevel);
        activity.startActivityForResult(intent, 2333);
    }

}
