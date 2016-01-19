package unicorn.com.xhsr.select;


import java.io.Serializable;

public class SelectObject implements Serializable {

    public String value;

    public String objectId;


    @Override
    public boolean equals(Object o) {
        if (o instanceof SelectObject) {
            SelectObject another = (SelectObject) o;
            return this.objectId.equals(another.objectId);
        }
        return false;
    }

}
