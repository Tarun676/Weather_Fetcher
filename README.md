# Weather Web Application

A modern, responsive weather dashboard built with Spring Boot and Java 21. This application provides real-time weather updates and forecasts with a stunning, dynamic user interface featuring glassmorphism and animated weather effects.

## 🌟 Features

-   **Real-time Weather Data**: Current temperature, humidity, wind speed, and "feels like" temperature.
-   **5-Day Forecast**: Accurate forecast data to plan your week.
-   **Dynamic UI**: Background animations change based on live weather conditions (Clear, Rain, Snow, Clouds, Thunder).
-   **Glassmorphism Design**: Modern, sleek, and responsive interface.
-   **City Search**: Easy-to-use search bar to check weather for any global location.
-   **Caching**: Optimized performance using Caffeine cache for API requests.
-   **Error Handling**: User-friendly error messages for invalid cities or API issues.

## 🛠️ Tech Stack

-   **Backend**: Java 21, Spring Boot 3.4.0
-   **Frontend**: HTML5, CSS3 (Custom Animations), Thymeleaf
-   **Build Tool**: Maven
-   **Containerization**: Docker, Docker Compose
-   **External APIs**: OpenWeatherMap

## 🚀 Getting Started

### Prerequisites

-   Java 21 JDK (for local development)
-   Maven (optional, wrapper included)
-   Docker & Docker Compose (for containerized run)
-   OpenWeatherMap API Key

### 🐳 Running with Docker (Recommended)

The easiest way to run the application is using Docker Compose.

1.  **Clone the repository**.
2.  **Run the application**:
    ```bash
    docker-compose up --build
    ```
3.  **Access the app**:
    Open [http://localhost:8080](http://localhost:8080) in your browser.

*Note: The API key is configured in `docker-compose.yml` via the `WEATHER_API_KEY` environment variable.*

### 💻 Running Locally (Maven)

1.  **Build the project**:
    ```bash
    ./mvnw clean package
    ```
2.  **Run the application**:
    ```bash
    ./mvnw spring-boot:run
    ```

## 📂 Project Structure

```
weather-web/
├── src/
│   ├── main/
│   │   ├── java/com/weather/      # Controllers, Services, Models
│   │   └── resources/
│   │       ├── static/css/        # Stylesheets & Animations
│   │       ├── templates/         # Thymeleaf HTML views
│   │       └── application.properties
│   └── test/                      # JUnit Tests
├── Dockerfile                     # Docker build configuration
├── docker-compose.yml             # Docker Compose configuration
└── pom.xml                        # Maven dependencies
```

## 🧪 Testing

Run the unit tests using Maven:
```bash
./mvnw test
```
