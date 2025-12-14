package com.weather;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service

public class WeatherService {
    private final String apiKey = "96cd2e039bb427d186d23a1371e855bc";

    public WeatherData fetchWeather(String city) throws Exception {
        String url = "https://api.openweathermap.org/data/2.5/weather?q="
                     + city + "&appid=" + apiKey + "&units=metric";

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();

        String jsonData = response.body().string();
        JSONObject obj = new JSONObject(jsonData);

        if (obj.getInt("cod") != 200) {
            throw new Exception(obj.getString("message"));
        }

        JSONObject main = obj.getJSONObject("main");
        JSONArray weatherArr = obj.getJSONArray("weather");
        JSONObject weather = weatherArr.getJSONObject(0);
        JSONObject wind = obj.getJSONObject("wind");
        JSONObject sys = obj.getJSONObject("sys");

        WeatherData data = new WeatherData();
        data.setCity(obj.getString("name"));
        data.setCountry(sys.getString("country"));
        data.setTemp(main.getDouble("temp"));
        data.setFeelsLike(main.getDouble("feels_like"));
        data.setHumidity(main.getInt("humidity"));
        data.setCondition(weather.getString("main"));
        data.setWindSpeed(wind.getDouble("speed"));

        return data;
    }
    public List<WeatherForecast> fetchForecast(String city) throws Exception {

    String url = "https://api.openweathermap.org/data/2.5/forecast?q="
            + city + "&appid=" + apiKey + "&units=metric";

    OkHttpClient client = new OkHttpClient();
    Request request = new Request.Builder().url(url).build();
    Response response = client.newCall(request).execute();

    String jsonData = response.body().string();
    JSONObject obj = new JSONObject(jsonData);

    JSONArray list = obj.getJSONArray("list");
    List<WeatherForecast> forecastList = new ArrayList<>();

    // One forecast per day (every 24 hrs)
    for (int i = 0; i < list.length(); i += 8) {
        JSONObject item = list.getJSONObject(i);

        String dateTime = item.getString("dt_txt");
        double temp = item.getJSONObject("main").getDouble("temp");
        String condition = item.getJSONArray("weather")
                               .getJSONObject(0)
                               .getString("main");

        forecastList.add(
            new WeatherForecast(dateTime, temp, condition)
        );
    }

    return forecastList;
}


}
