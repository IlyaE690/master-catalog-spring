package kfu.itis.controller.api;

import kfu.itis.service.AiOrderSuggestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class AiSuggestionController {

    private final AiOrderSuggestionService aiOrderSuggestionService;

    public AiSuggestionController(AiOrderSuggestionService aiOrderSuggestionService) {
        this.aiOrderSuggestionService = aiOrderSuggestionService;
    }

    @PostMapping("/ai-suggest")
    public ResponseEntity<Map<String, Object>> suggest(@RequestBody Map<String, Object> body) {
        String issueDescription = body.getOrDefault("issueDescription", "").toString();
        Long specializationId = Long.valueOf(body.get("specializationId").toString());
        Double minRating = body.get("minRating") == null ? null : Double.valueOf(body.get("minRating").toString());

        return ResponseEntity.ok(aiOrderSuggestionService.buildSuggestion(issueDescription, specializationId, minRating));
    }
}
