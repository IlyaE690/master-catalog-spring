package kfu.itis.controller;

import kfu.itis.service.WeatherService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final WeatherService weatherService;

    public HomeController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("weatherSummary", weatherService.getWeatherSummaryForKazan());
        return "home";
    }
}
