    package com.weather;
    import java.util.List;

    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.RequestParam;

    @Controller
    public class WeatherController {

        private static final String DEFAULT_CITY = "Phagwara";

        @Autowired
        private WeatherService weatherService;

        @GetMapping("/")
        public String home() {
            // load default city on root access
            return "redirect:/weather?city=" + DEFAULT_CITY;
        }

        @GetMapping("/weather")
        public String getWeather(@RequestParam(name = "city", required = false) String city, Model model) {
            if (city == null || city.trim().isEmpty()) {
                city = DEFAULT_CITY;
            }
            try {
                WeatherData data = weatherService.fetchWeather(city);
                List<WeatherForecast> forecast = weatherService.fetchForecast(city);

                model.addAttribute("weather", data);
                model.addAttribute("forecast", forecast);

            } catch (Exception e) {
                model.addAttribute("error", e.getMessage());
            }
            return "index";
        }

    }
