package unicorn.com.xhsr.groupselect;


import unicorn.com.xhsr.SelectObject;

public class GroupSelectHelper {

    public static GroupSelectObject create(int level, int position,String value) {
        SelectObject selectObject = new SelectObject();
        selectObject.position = position;
        selectObject.value =value;
        GroupSelectObject groupSelectObject = new GroupSelectObject();
        groupSelectObject.selectObject = selectObject;
        groupSelectObject.level = level;
        return groupSelectObject;
    }

}
