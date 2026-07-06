package kfu.itis.controller.api;

import kfu.itis.model.dto.SuggestionResponseDto;
import kfu.itis.service.OrderSuggestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class SuggestionController {

    private final OrderSuggestionService aiOrderSuggestionService;

    public SuggestionController(OrderSuggestionService aiOrderSuggestionService) {
        this.aiOrderSuggestionService = aiOrderSuggestionService;
    }

    @PostMapping("/ai-suggest")
    public ResponseEntity<SuggestionResponseDto> suggest(@RequestBody Map<String, Object> body) {
        String issueDescription = body.getOrDefault("issueDescription", "").toString();
        Long specializationId = body.get("specializationId") == null || body.get("specializationId").toString().isBlank()
                ? null
                : Long.valueOf(body.get("specializationId").toString());
        Double minRating = body.get("minRating") == null ? null : Double.valueOf(body.get("minRating").toString());

        SuggestionResponseDto suggestion = specializationId == null
                ? aiOrderSuggestionService.buildSuggestion(issueDescription, minRating)
                : aiOrderSuggestionService.buildSuggestion(issueDescription, specializationId, minRating);
        return ResponseEntity.ok(suggestion);
    }
}
