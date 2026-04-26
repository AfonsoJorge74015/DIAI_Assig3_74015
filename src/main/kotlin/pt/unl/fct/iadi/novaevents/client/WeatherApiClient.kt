package pt.unl.fct.iadi.novaevents.client

import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.service.annotation.GetExchange

interface WeatherApiClient {
    @GetExchange("/data/2.5/weather")
    fun getWeather(
        @RequestParam("q") location: String,
        @RequestParam("appid") apiKey: String
    ): WeatherResponse
}

data class WeatherResponse(val weather: List<WeatherDescription>)
data class WeatherDescription(val main: String)