package pt.unl.fct.iadi.novaevents.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import pt.unl.fct.iadi.novaevents.client.WeatherApiClient

@Service
class WeatherService(
    private val weatherApiClient: WeatherApiClient,
    @Value("\${weather.api.key}") private val apiKey: String
) {
    fun isRaining(location: String): Boolean? {
        return try {
            val response = weatherApiClient.getWeather(location, apiKey)
            val main = response.weather.firstOrNull()?.main ?: return null
            main.equals("Rain", ignoreCase = true)
                    || main.equals("Drizzle", ignoreCase = true)
                    || main.equals("Thunderstorm", ignoreCase = true)
        } catch (e: Exception) {
            null // Return null if API fails or location is invalid
        }
    }
}