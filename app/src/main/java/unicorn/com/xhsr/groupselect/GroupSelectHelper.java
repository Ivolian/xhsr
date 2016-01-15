package unicorn.com.xhsr.groupselect;


import android.app.Activity;
import android.content.Intent;

import unicorn.com.xhsr.SelectObject;

public class GroupSelectHelper {

    public static String RESULT = "result";

    public static GroupSelectObject createGroupSelectObject(int level, int position, String value) {
        SelectObject selectObject = new SelectObject();
        selectObject.position = position;
        selectObject.value = value;
        GroupSelectObject groupSelectObject = new GroupSelectObject();
        groupSelectObject.selectObject = selectObject;
        groupSelectObject.level = level;
        return groupSelectObject;
    }

    public static void startGroupSelectActivity(Activity activity, String name, int maxLevel) {
        Intent intent = new Intent(activity, GroupSelectActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("maxLevel", maxLevel);
        activity.startActivityForResult(intent, 2333);
    }

}
