package com.weather.weatherapp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weather.weatherapp.exception.CityNotValidException;
import com.weather.weatherapp.model.CurrentWeather;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;

import java.math.BigDecimal;
import java.net.URI;

/**
 * Weather service (facade) to send request to the external weather API - Open Weather Map.
 */
@Service
public class WeatherServiceClient {
    @Value("${api.url}")
    public String apiUrl;

    @Value("${api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public WeatherServiceClient(RestTemplateBuilder restTemplateBuilder, ObjectMapper objectMapper) {
        this.restTemplate = restTemplateBuilder.build();
        this.objectMapper = objectMapper;
    }

    /**
     * Call external weather service Open Weather Map to get data about current weather in the requested city.
     *
     * @param city the city for which we want to receive current weather data
     * @return object with current weather data.
     */
    public CurrentWeather getCurrentWeather(String city) {
        if(!isCityValid(city)){
            throw new CityNotValidException(city);
        }
        URI url = new UriTemplate(apiUrl).expand(city, apiKey);
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return convert(response);
    }

    private CurrentWeather convert(ResponseEntity<String> response) {
        try {
            JsonNode root = objectMapper.readTree(response.getBody());

            String conditionDescription = root.path("weather").get(0).path("description").asText();
            String temperatureValue = root.path("main").path("temp").asText();
            BigDecimal windSpeedValue = BigDecimal.valueOf(root.path("wind").path("speed").asDouble());

            return createCurrentWeather(conditionDescription, temperatureValue, windSpeedValue);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error parsing JSON", e);
        }
    }

    private static CurrentWeather createCurrentWeather(String conditionDescription, String temperatureValue, BigDecimal windSpeedValue) {
        CurrentWeather.Properties.Detail condition = new CurrentWeather.Properties.Detail("string", conditionDescription);
        CurrentWeather.Properties.Detail temperature = new CurrentWeather.Properties.Detail("number", temperatureValue);
        CurrentWeather.Properties.FullDetail windSpeed = CurrentWeather.Properties.FullDetail.builder().type("number").description("Speed of the wind in km/h.").minimum(windSpeedValue).build();
        CurrentWeather.Properties properties = CurrentWeather.Properties.builder().condition(condition).temperature(temperature).wind_speed(windSpeed).build();

        return CurrentWeather.builder().title("Weather").type("object").properties(properties).build();
    }

    /**
     * City is valid if it consists of group of letters OR multiple groups of letters separated by whitespace
     * @param city
     * @return
     */
    private static boolean isCityValid(String city) {
        return city.matches("([a-zA-Z]+|[a-zA-Z]+\\s[a-zA-Z]+)");
    }
}
