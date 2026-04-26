package pt.unl.fct.iadi.novaevents

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.springframework.boot.test.context.SpringBootTest
import pt.unl.fct.iadi.novaevents.client.WeatherApiClient
import pt.unl.fct.iadi.novaevents.client.WeatherDescription
import pt.unl.fct.iadi.novaevents.client.WeatherResponse
import pt.unl.fct.iadi.novaevents.service.WeatherService

@SpringBootTest
class Assignment3ApplicationTests {

    private val mockApiClient = mock(WeatherApiClient::class.java)
    private val weatherService = WeatherService(mockApiClient, "dummy_key")

    @Test
    fun testRainingWeather() {
        `when`(mockApiClient.getWeather("London", "dummy_key"))
            .thenReturn(WeatherResponse(listOf(WeatherDescription("Rain"))))

        assertTrue(weatherService.isRaining("London")!!)
    }

    @Test
    fun testClearWeather() {
        `when`(mockApiClient.getWeather("Lisbon", "dummy_key"))
            .thenReturn(WeatherResponse(listOf(WeatherDescription("Clear"))))

        assertFalse(weatherService.isRaining("Lisbon")!!)
    }

}
