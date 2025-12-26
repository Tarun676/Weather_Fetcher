# How to Run Weather Web with Docker

This project is containerized and can be easily run using Docker.

## Prerequisites

- [Docker Desktop](https://www.docker.com/products/docker-desktop/) installed and running.

## Option 1: Using Docker Compose (Recommended)

This is the simplest method as it handles building and running in one command.

1. Open a terminal in the project root directory.
2. Run the following command:

   ```bash
   docker-compose up --build
   ```

3. Wait for the build to complete and the application to start. You should see logs indicating the Spring Boot application has started.

## Option 2: Manual Build and Run

If you prefer to run `docker` commands manually:

1. **Build the Image:**

   ```bash
   docker build -t weather-web .
   ```

2. **Run the Container:**

   ```bash
   docker run -p 8080:8080 weather-web
   ```

## Accessing the Application

Once the application is running, open your web browser and navigate to:

[http://localhost:8080](http://localhost:8080)

## Configuration

The API Key is currently configured in `docker-compose.yml`:
```yaml
environment:
  - WEATHER_API_KEY=96cd2e039bb427d186d23a1371e855bc
```
If you need to change it, you can edit the `docker-compose.yml` file or pass it as an environment variable in the `docker run` command:
```bash
docker run -p 8080:8080 -e WEATHER_API_KEY=your_key_here weather-web
```
