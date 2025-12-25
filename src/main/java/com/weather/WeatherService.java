package com.weather;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WeatherService {

    private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);

    private final OkHttpClient client;

    @Value("${weather.api.key}")
    private String apiKey;

    @Value("${weather.api.url}")
    private String apiUrl;

    public WeatherService(OkHttpClient client) {
        this.client = client;
    }

    @Cacheable("weather")
    public WeatherData fetchWeather(String city) throws Exception {
        logger.info("Fetching weather for city: {}", city);
        String url = String.format("%s/weather?q=%s&appid=%s&units=metric", apiUrl, city, apiKey);

        Request request = new Request.Builder().url(url).build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new Exception("Error fetching weather: " + response.message());
            }
            if (response.body() == null) {
                throw new Exception("Empty response body");
            }

            String jsonData = response.body().string();
            JSONObject obj = new JSONObject(jsonData);

            if (obj.has("cod") && obj.getInt("cod") != 200) {
                throw new Exception(obj.optString("message", "Unknown error"));
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
    }

    @Cacheable("forecast")
    public List<WeatherForecast> fetchForecast(String city) throws Exception {
        logger.info("Fetching forecast for city: {}", city);
        String url = String.format("%s/forecast?q=%s&appid=%s&units=metric", apiUrl, city, apiKey);

        Request request = new Request.Builder().url(url).build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new Exception("Error fetching forecast: " + response.message());
            }
            if (response.body() == null) {
                throw new Exception("Empty response body");
            }

            String jsonData = response.body().string();
            JSONObject obj = new JSONObject(jsonData);

            // Check for API errors in body (sometimes 200 OK but cod != "200" string)
            if (obj.has("cod")) {
                String cod = obj.get("cod").toString();
                if (!"200".equals(cod)) {
                    throw new Exception(obj.optString("message", "Unknown error"));
                }
            }

            JSONArray list = obj.getJSONArray("list");
            List<WeatherForecast> forecastList = new ArrayList<>();

            // One forecast per day (approx every 8th item as 3hr intervals * 8 = 24hrs)
            for (int i = 0; i < list.length(); i += 8) {
                JSONObject item = list.getJSONObject(i);

                String dateTime = item.getString("dt_txt");
                double temp = item.getJSONObject("main").getDouble("temp");
                String condition = item.getJSONArray("weather")
                        .getJSONObject(0)
                        .getString("main");

                WeatherForecast forecast = new WeatherForecast();
                forecast.setDateTime(dateTime);
                forecast.setTemp(temp);
                forecast.setCondition(condition);

                // --- DATE FORMATTING LOGIC ---
                try {
                    // dt_txt format: "yyyy-MM-dd HH:mm:ss"
                    java.time.LocalDateTime dt = java.time.LocalDateTime.parse(dateTime, java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    
                    // Day Name: "Monday"
                    String day = dt.getDayOfWeek().getDisplayName(java.time.format.TextStyle.SHORT, java.util.Locale.ENGLISH); 
                    forecast.setDayName(day);
                    
                    // Readable Date: "25-12-25" (dd-MM-yy)
                    String dateStr = dt.format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yy"));
                    forecast.setReadableDate(dateStr);
                    
                } catch (Exception e) {
                    logger.error("Error parsing date: {}", dateTime, e);
                    forecast.setDayName("---");
                    forecast.setReadableDate("--:--");
                }

                forecastList.add(forecast);
            }

            return forecastList;
        }
    }
}
