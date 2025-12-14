package com.weather;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.awt.Desktop;
import java.net.URI;

@SpringBootApplication
public class WeatherWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeatherWebApplication.class, args);

        // Automatically open default browser
        try {
            String url = "http://localhost:8080/";
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(new URI(url));
            }
        } catch (Exception e) {
            System.out.println("Could not open browser: " + e.getMessage());
        }
    }
}
