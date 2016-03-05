package unicorn.com.xhsr.weather;

import java.util.HashMap;
import java.util.Map;


public class WeatherMap {

    public static Map<String, String> getMap() {
        return map;
    }

    private static Map<String,String> map  = new HashMap<>();

    static {

        // 1**
        map.put("晴","wic-day-sunny");
        map.put("多云","wic-cloudy");
        map.put("少云","wic-cloud");
        map.put("晴间多云","wic-day-cloudy");
        map.put("阴","wic-solar-eclipse");

        // 2**
        map.put("有风","wic-cloudy-windy");
        map.put("微风","wic-cloudy-windy");
        map.put("和风","wic-cloudy-windy");
        map.put("清风","wic-cloudy-windy");
        map.put("疾风","wic-cloudy-gusts");
        map.put("大风","wic-cloudy-gusts");
        map.put("烈风","wic-cloudy-gusts");
        map.put("风暴","wic-hurricane");
        map.put("狂爆风","wic-hurricane");
        map.put("飓风","wic-hurricane");
        map.put("龙卷风","wic-tornado");
        map.put("热带风暴","wic-tornado");

        // 3**
        map.put("阵雨","wic-rain");
        map.put("强阵雨","wic-showers");
        map.put("雷阵雨","wic-thunderstorm");
        map.put("强雷阵雨","wic-thunderstorm");
        map.put("雷阵雨伴有冰雹","wic-storm-showers");
        map.put("小雨","wic-rain");
        map.put("中雨","wic-rain");
        map.put("大雨","wic-showers");
        map.put("暴雨","wic-showers");
        map.put("大暴雨","wic-showers");
        map.put("特大暴雨","wic-showers");
        map.put("冻雨","wic-hail");

        // 4**
        map.put("小雪","wic-snow");
        map.put("中雪","wic-snow");
        map.put("大雪","wic-snow");
        map.put("暴雪","wic-snow");
        map.put("雨夹雪","wic-sleet");
        map.put("雨雪天气","wic-sleet");
        map.put("阵雨夹雪","wic-sleet");
        map.put("阵雪","wic-sleet");

        // 5**
        map.put("薄雾","wic-fog");
        map.put("雾","wic-fog");
        map.put("霾","wic-day-haze");
        map.put("扬沙","wic-dust");
        map.put("浮尘","wic-dust");
        map.put("沙尘暴","wic-sandstorm");
        map.put("强沙尘暴","wic-sandstorm");

        // 9**
        map.put("未知","wic-na");
    }

}
