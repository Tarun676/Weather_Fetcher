package com.weather;

public class WeatherForecast {

    private String dateTime;
    private double temp;
    private String condition;

    public WeatherForecast(String dateTime, double temp, String condition) {
        this.dateTime = dateTime;
        this.temp = temp;
        this.condition = condition;
    }

    public String getDateTime() {
        return dateTime;
    }

    public double getTemp() {
        return temp;
    }

    public String getCondition() {
        return condition;
    }
}
