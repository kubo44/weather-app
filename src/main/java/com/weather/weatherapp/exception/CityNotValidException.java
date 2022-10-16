package com.weather.weatherapp.exception;

public class CityNotValidException extends RuntimeException {
    public CityNotValidException(String cityName) {
        super(String.format("City name '%s' is not valid", cityName));
    }
}
