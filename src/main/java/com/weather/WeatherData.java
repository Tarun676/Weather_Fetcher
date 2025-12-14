package com.weather;

    public class WeatherData {
    private String city;
    private String country;
    private double temp;
    private double feelsLike;
    private int humidity;
    private String condition;
    private double windSpeed;

    public void setCity(String city) { this.city = city; }
    public void setCountry(String country) { this.country = country; }
    public void setTemp(double temp) { this.temp = temp; }
    public void setFeelsLike(double feelsLike) { this.feelsLike = feelsLike; }
    public void setHumidity(int humidity) { this.humidity = humidity; }
    public void setCondition(String condition) { this.condition = condition; }
    public void setWindSpeed(double windSpeed) { this.windSpeed = windSpeed; }

    public String getCity() { return city; }
    public String getCountry() { return country; }
    public double getTemp() { return temp; }
    public double getFeelsLike() { return feelsLike; }
    public int getHumidity() { return humidity; }
    public String getCondition() { return condition; }
    public double getWindSpeed() { return windSpeed; }
}
