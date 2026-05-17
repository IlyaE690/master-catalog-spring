package kfu.itis.controller;

import kfu.itis.service.WeatherService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.ui.ConcurrentModel;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HomeControllerTest {

    @Test
    void homeAddsWeatherSummary() {
        WeatherService weatherService = Mockito.mock(WeatherService.class);
        Mockito.when(weatherService.getWeatherSummaryForKazan()).thenReturn("test");

        HomeController controller = new HomeController(weatherService);
        ConcurrentModel model = new ConcurrentModel();

        String view = controller.home(model);

        assertEquals("home", view);
        assertEquals("test", model.getAttribute("weatherSummary"));
    }
}
