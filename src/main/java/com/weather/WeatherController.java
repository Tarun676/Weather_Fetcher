package com.weather;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class WeatherController {

    private static final Logger log = LoggerFactory.getLogger(WeatherController.class);
    private static final String DEFAULT_CITY = "Phagwara";

    @Autowired
    private WeatherService weatherService;

    @GetMapping("/")
    public String home() {
        return "redirect:/weather?city=" + DEFAULT_CITY;
    }

    @GetMapping("/weather")
    public String getWeather(@RequestParam(name = "city", required = false) String city, Model model) {
        if (city == null || city.trim().isEmpty()) {
            city = DEFAULT_CITY;
        }

        log.info("Requesting weather for city: {}", city);

        try {
            WeatherData data = weatherService.fetchWeather(city);
            List<WeatherForecast> forecast = weatherService.fetchForecast(city);

            model.addAttribute("weather", data);
            model.addAttribute("forecast", forecast);

        } catch (Exception e) {
            log.error("Error fetching weather data: {}", e.getMessage());
            String errorMsg = e.getMessage().toLowerCase();
            
            if (errorMsg.contains("not found") || errorMsg.contains("404")) {
                 model.addAttribute("error", "City not found");
            } else {
                 model.addAttribute("error", "Error: " + e.getMessage());
            }
        }
        return "index";
    }

    @ExceptionHandler(Exception.class)
    public String handleError(Exception e, Model model) {
        log.error("Global error: ", e);
        model.addAttribute("error", "An unexpected error occurred: " + e.getMessage());
        return "index";
    }

}
