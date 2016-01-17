package unicorn.com.xhsr.groupselect;


import android.app.Activity;
import android.content.Intent;

import unicorn.com.xhsr.select.SelectHelper;
import unicorn.com.xhsr.select.SelectObject;
import unicorn.com.xhsr.select.SelectObjectWithPosition;

public class GroupSelectHelper {

    public static String RESULT = "result";

    public static GroupSelectObject createGroupSelectObject(int level, int position, SelectObject selectObject) {
        SelectObjectWithPosition selectObjectWithPosition = SelectHelper.create(selectObject,position);
        GroupSelectObject groupSelectObject = new GroupSelectObject();
        groupSelectObject.selectObjectWithPosition = selectObjectWithPosition;
        groupSelectObject.level = level;
        return groupSelectObject;
    }

    public static void startGroupSelectActivity(Activity activity, String name, int maxLevel,int resultCode) {
        Intent intent = new Intent(activity, GroupSelectActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("maxLevel", maxLevel);
        intent.putExtra("resultCode",resultCode);
        activity.startActivityForResult(intent, 2333);
    }

}
