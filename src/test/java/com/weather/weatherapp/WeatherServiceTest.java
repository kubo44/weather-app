package com.weather.weatherapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weather.weatherapp.model.CurrentWeather;
import com.weather.weatherapp.service.WeatherServiceClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.util.UriTemplate;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
@RestClientTest(WeatherServiceClient.class)
public class WeatherServiceTest {
    @Autowired
    private WeatherServiceClient client;

    @Autowired
    private MockRestServiceServer server;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${api.url}")
    public String apiUrl;
    @Value("${api.key}")
    private String apiKey;

    private String testCity = "mering";
    private float testWindSpeed = 25;
    private float testTemperature = 18;
    private String testDescription = "blabla";

    @Before
    public void setUp() throws Exception {
        String detailsString =
                objectMapper.writeValueAsString(new WeatherDto(testWindSpeed, testDescription, testTemperature));

        URI url = new UriTemplate(apiUrl).expand(testCity, apiKey);
        this.server.expect(requestTo(url.toString()))
                .andRespond(withSuccess(detailsString, MediaType.APPLICATION_JSON));
    }

    @Test
    public void whenCallingGetCurrentWeather_thenClientMakesCorrectCall()
            throws Exception {

        CurrentWeather currentWeather = this.client.getCurrentWeather(testCity);

        assertThat(currentWeather.getTitle()).isEqualTo("Weather");
        assertThat(currentWeather.getType()).isEqualTo("object");
        assertThat(currentWeather.getProperties().getCondition().getDescription()).isEqualTo(testDescription);
        assertThat(currentWeather.getProperties().getTemperature().getDescription()).isEqualTo(String.valueOf(testTemperature));
        assertThat(currentWeather.getProperties().getWind_speed().getMinimum().floatValue()).isEqualTo(testWindSpeed);
    }
}