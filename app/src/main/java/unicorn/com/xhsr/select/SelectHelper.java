package unicorn.com.xhsr.select;

/**
 * Created by Administrator on 2016/1/17.
 */
public class SelectHelper {

    public static SelectObjectWithPosition create(SelectObject selectObject,int position){

        SelectObjectWithPosition selectObjectWithPosition = new SelectObjectWithPosition();
        selectObjectWithPosition.position = position;
        selectObjectWithPosition.value = selectObject.value;
        selectObjectWithPosition.objectId = selectObject.objectId;
        return selectObjectWithPosition;
    }

}
