package unicorn.com.xhsr.weather.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/3/4.
 */
public class Aqi implements Serializable {

    private City city;

    //

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }
}
