package com.weather.weatherapp;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class WeatherDto {

    private final Wind wind;
    private final List<Description> weather;
    private final Main main;

    public WeatherDto(float speed, String description, float temperature){
        wind = new Wind(speed);
        weather = new ArrayList<>();
        weather.add(new Description(description));
        main = new Main(temperature);
    }

    @Data
    private class Wind{
        private final float speed;
    }

    @Data
    private class Description{
        private final String description;
    }

    @Data
    private class Main{
        private final float temp;
    }
}
