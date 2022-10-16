package com.weather.weatherapp.model;

import lombok.*;

import java.math.BigDecimal;

/**
 * DTO to store details about current weather.
 */
@Builder
@Data
public final class CurrentWeather {
    private final String title;
    private final String type;
    private final Properties properties;

    @Builder
    @Data
    public static final class Properties {
        private final Detail condition;
        private final Detail temperature;
        private final FullDetail wind_speed;

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class Detail {
            private String type;
            private String description;
        }

        @Data
        public static class FullDetail extends Detail {
            private final BigDecimal minimum;

            @Builder
            public FullDetail(String type, String description, BigDecimal minimum) {
                super(type, description);
                this.minimum = minimum;
            }
        }
    }
}