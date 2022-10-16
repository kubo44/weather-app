package com.weather.weatherapp.controller;

import com.weather.weatherapp.model.CurrentWeather;
import com.weather.weatherapp.service.WeatherServiceClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Controller to handle weather related requests.
 */
@RestController
@Validated
public class WeatherController {

    private final WeatherServiceClient weatherServiceClient;

    public WeatherController(WeatherServiceClient weatherServiceClient) {
        this.weatherServiceClient = weatherServiceClient;
    }

    /**
     * Endpoint to handle requests to get current weather for given city.
     * @param city
     * @return CurrentWeather Dto or BadRequest if city is not valid
     */
    @GetMapping("/weather/{city}")
    public ResponseEntity<CurrentWeather> getCurrentWeather(@PathVariable @NotBlank @Size(min = 3) String city) {
        CurrentWeather currentWeather = weatherServiceClient.getCurrentWeather(city);
        return ResponseEntity.ok(currentWeather);
    }
}
