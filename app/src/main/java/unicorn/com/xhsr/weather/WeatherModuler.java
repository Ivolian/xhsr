package unicorn.com.xhsr.weather;


import android.net.Uri;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONObject;

import unicorn.com.xhsr.other.TinyDB;
import unicorn.com.xhsr.utils.SharedPreferencesUtils;
import unicorn.com.xhsr.volley.SimpleVolley;
import unicorn.com.xhsr.weather.model.WeatherInfo;

public class WeatherModuler {

    public interface SimpleInterface {
        void afterFetchWeatherInfo();
    }

    private SimpleInterface simpleInterface;

    public WeatherModuler(SimpleInterface simpleInterface) {
        this.simpleInterface = simpleInterface;
    }

    public void fetchWeatherInfo() {
        if (isWeatherUpdated()) {
            simpleInterface.afterFetchWeatherInfo();
            return;
        }
        String url = getUrl();
        JsonObjectRequest request = new JsonObjectRequest(
                url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // 这里我们假设 API 的接口不会变，接口也不会无故不可用
                        try {
                            JSONArray heWeatherDataService = response.getJSONArray("HeWeather data service 3.0");
                            String weatherInfoString = heWeatherDataService.getJSONObject(0).toString();
                            WeatherInfo weatherInfo = new Gson().fromJson(weatherInfoString, WeatherInfo.class);
                            String updateDateString = new DateTime().toString("yyyyMMdd");
                            weatherInfo.setUpdateDateString(updateDateString);
                            TinyDB.getInstance().putObject(SharedPreferencesUtils.WEATHER_INFO, weatherInfo);
                            simpleInterface.afterFetchWeatherInfo();
                        } catch (Exception e) {
                            //
                        }
                    }
                },
                SimpleVolley.getDefaultErrorListener()

        );
        SimpleVolley.addRequest(request);
    }


    private boolean isWeatherUpdated() {
        String dateString = new DateTime().toString("yyyyMMdd");
        WeatherInfo weatherInfo = (WeatherInfo) TinyDB.getInstance().getObject(SharedPreferencesUtils.WEATHER_INFO, WeatherInfo.class);
        return weatherInfo != null && weatherInfo.getUpdateDateString().endsWith(dateString);
    }


    private String getUrl() {
        String weatherUrl = "https://api.heweather.com/x3/weather";
        String apiKey = "3b85969c4b1a40ec8a94cac4f4fb454e";
        String cityId = "CN101010100";
        Uri.Builder builder = Uri.parse(weatherUrl).buildUpon();
        builder.appendQueryParameter("key", apiKey);
        builder.appendQueryParameter("cityid", cityId);
        return builder.toString();
    }



}
