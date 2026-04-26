package pt.unl.fct.iadi.novaevents.client

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.service.annotation.GetExchange

interface WeatherApiClient {
    @GetExchange("/data/2.5/weather")
    fun getWeather(
        @RequestParam("q") location: String,
        @RequestParam("appid") apiKey: String
    ): WeatherResponse
}
@JsonIgnoreProperties(ignoreUnknown = true)
data class WeatherResponse(val weather: List<WeatherDescription> = emptyList())

@JsonIgnoreProperties(ignoreUnknown = true)
data class WeatherDescription(val main: String = "")