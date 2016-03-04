package unicorn.com.xhsr.weather.model;


import java.io.Serializable;

public class Now implements Serializable {

    private Cond cond;

    private String tmp;

    //

    public Cond getCond() {
        return cond;
    }

    public void setCond(Cond cond) {
        this.cond = cond;
    }

    public String getTmp() {
        return tmp;
    }

    public void setTmp(String tmp) {
        this.tmp = tmp;
    }
}
