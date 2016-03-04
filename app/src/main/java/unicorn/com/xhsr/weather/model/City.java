package unicorn.com.xhsr.weather.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/3/4.
 */
public class City implements Serializable {

    private String pm25;

    private String qlty;

    //

    public String getPm25() {
        return pm25;
    }

    public void setPm25(String pm25) {
        this.pm25 = pm25;
    }

    public String getQlty() {
        return qlty;
    }

    public void setQlty(String qlty) {
        this.qlty = qlty;
    }
}
