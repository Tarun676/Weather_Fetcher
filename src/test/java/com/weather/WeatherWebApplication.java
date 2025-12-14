package com.weather;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.event.EventListener;
import org.springframework.boot.context.event.ApplicationReadyEvent;

import java.awt.Desktop;
import java.net.URI;

@SpringBootApplication
public class WeatherWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeatherWebApplication.class, args);
    }

    // This method will run when the app is fully started
    @EventListener(ApplicationReadyEvent.class)
    public void openBrowser() {
        try {
            String url = "http://localhost:8080/";
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(new URI(url));
            } else {
                System.out.println("Desktop not supported. Open browser manually: " + url);
            }
        } catch (Exception e) {
            System.out.println("Could not open browser: " + e.getMessage());
        }
    }
}
