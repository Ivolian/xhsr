package unicorn.com.xhsr;

import java.util.HashMap;
import java.util.Map;


public class WeatherMap {


    public static Map<String, String> getMap() {
        return map;
    }

    private static Map<String,String> map  = new HashMap<>();

    static {
        map.put("晴","wic-day-sunny");
        map.put("多云","wic-cloudy");
        map.put("晴间多云","wic-day-cloudy");
        map.put("阴","wic-cloud");
        map.put("雾","wic-fog");
        map.put("霾","wic-day-haze");
        map.put("大风","wic-strong-wind");
        map.put("龙卷风","wic-tornado");
        map.put("阵雨","wic-day-rain-mix");
        map.put("雨","wic-showers");
        map.put("大雨","wic-rain");
        map.put("暴风雪","wic-thunderstorm");
        map.put("雨夹雪","wic-rain-mix");
        map.put("阵雪","wic-day-snow-wind");
        map.put("小雪","wic-snow");
    }

}
