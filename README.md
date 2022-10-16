## Goal
Spring Boot application in Java which returns the current weather information for a city using the OpenWeather API.

## OpenWeather API
Uses the current open weather API https://openweathermap.org/current

### Weather API
Web Application:
- Accept a city name via REST API
- Get the current weather for the city name using the OpenWeather API
- Return the response as JSON following this **[JSON schema](https://json-schema.org/)**:
```
{
  "title": "Weather",
  "type": "object",
  "properties": {
    "condition": {
      "type": "string",
      "description": "The description of the weather. eg: scattered clouds."
    },
    "temperature": {
      "type": "number",
      "description": "The actual temperature of the city in celsius."
    },
    "wind_speed": {
      "type": "number",
      "description": "Speed of the wind in km/h.",
      "minimum": 0
    }
  }
}
```
- Validates the input of the REST API allow only meaningful data and handle invalid requests.