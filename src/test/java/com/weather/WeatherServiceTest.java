package com.weather;

import okhttp3.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Disabled("Java 25 incompatibility with Mockito/ByteBuddy")
class WeatherServiceTest {

    @Mock
    private OkHttpClient client;

    @Mock
    private Call call;

    @InjectMocks
    private WeatherService weatherService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Inject values manually since we are not loading Spring Context here
        ReflectionTestUtils.setField(weatherService, "apiKey", "test-key");
        ReflectionTestUtils.setField(weatherService, "apiUrl", "http://test-api.com");
    }

    @Test
    void fetchWeather_Success() throws Exception {
        // Arrange
        String jsonResponse = "{"
                + "\"cod\": 200,"
                + "\"name\": \"London\","
                + "\"sys\": { \"country\": \"GB\" },"
                + "\"main\": { \"temp\": 15.5, \"feels_like\": 14.0, \"humidity\": 60 },"
                + "\"weather\": [ { \"main\": \"Clouds\" } ],"
                + "\"wind\": { \"speed\": 3.5 }"
                + "}";

        ResponseBody responseBody = ResponseBody.create(jsonResponse, MediaType.get("application/json"));
        Response response = new Response.Builder()
                .request(new Request.Builder().url("http://test-api.com").build())
                .protocol(Protocol.HTTP_1_1)
                .code(200)
                .message("OK")
                .body(responseBody)
                .build();

        when(client.newCall(any(Request.class))).thenReturn(call);
        when(call.execute()).thenReturn(response);

        // Act
        WeatherData data = weatherService.fetchWeather("London");

        // Assert
        assertNotNull(data);
        assertEquals("London", data.getCity());
        assertEquals("GB", data.getCountry());
        assertEquals(15.5, data.getTemp());
        assertEquals("Clouds", data.getCondition());
    }

    @Test
    void fetchForecast_Success() throws Exception {
        // Arrange
        String jsonResponse = "{"
                + "\"cod\": \"200\","
                + "\"list\": ["
                + "  { \"dt_txt\": \"2023-10-01 12:00:00\", \"main\": { \"temp\": 12.0 }, \"weather\": [ { \"main\": \"Rain\" } ] },"
                + "  {}, {}, {}, {}, {}, {}, {}," // need 8 items to get the first one
                + "  { \"dt_txt\": \"2023-10-02 12:00:00\", \"main\": { \"temp\": 13.0 }, \"weather\": [ { \"main\": \"Sun\" } ] }"
                + "]"
                + "}";

        // Note: The loop in service is i += 8.
        // Index 0: 2023-10-01 (captured)
        // Index 8: 2023-10-02 (captured)
        // Ensure list is long enough.
        // We can just construct a JSON with enough items or use a simplified mocked
        // loop logic?
        // No, we must match logic. We need at least 1 item. Logic: i < list.length().
        // If length is 1, it gets index 0. i+=8 => 8. Stop.
        // So 1 item is enough to test parsing of at least one forecast.

        ResponseBody responseBody = ResponseBody.create(jsonResponse, MediaType.get("application/json"));
        Response response = new Response.Builder()
                .request(new Request.Builder().url("http://test-api.com").build())
                .protocol(Protocol.HTTP_1_1)
                .code(200)
                .message("OK")
                .body(responseBody)
                .build();

        when(client.newCall(any(Request.class))).thenReturn(call);
        when(call.execute()).thenReturn(response);

        // Act
        List<WeatherForecast> forecasts = weatherService.fetchForecast("London");

        // Assert
        assertNotNull(forecasts);
        assertTrue(forecasts.size() >= 1);
        assertEquals("Rain", forecasts.get(0).getCondition());
        assertEquals(12.0, forecasts.get(0).getTemp());
    }
}
