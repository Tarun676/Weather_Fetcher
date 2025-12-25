package com.weather;

public class WeatherForecast {
    private String dateTime; // Original dt_txt
    private double temp;
    private String condition;
    private String dayName;      // e.g. "Friday"
    private String readableDate; // e.g. "25-12-25"

    public WeatherForecast() {
    }

    public WeatherForecast(String dateTime, double temp, String condition, String dayName, String readableDate) {
        this.dateTime = dateTime;
        this.temp = temp;
        this.condition = condition;
        this.dayName = dayName;
        this.readableDate = readableDate;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

    public String getReadableDate() {
        return readableDate;
    }

    public void setReadableDate(String readableDate) {
        this.readableDate = readableDate;
    }
}
