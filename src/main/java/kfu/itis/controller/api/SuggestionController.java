package kfu.itis.controller.api;

import kfu.itis.model.dto.AiSuggestionResponseDto;
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
    public ResponseEntity<AiSuggestionResponseDto> suggest(@RequestBody Map<String, Object> body) {
        String issueDescription = body.getOrDefault("issueDescription", "").toString();
        Long specializationId = body.get("specializationId") == null || body.get("specializationId").toString().isBlank()
                ? null
                : Long.valueOf(body.get("specializationId").toString());
        Double minRating = body.get("minRating") == null ? null : Double.valueOf(body.get("minRating").toString());

        AiSuggestionResponseDto suggestion = specializationId == null
                ? aiOrderSuggestionService.buildSuggestion(issueDescription, minRating)
                : aiOrderSuggestionService.buildSuggestion(issueDescription, specializationId, minRating);
        return ResponseEntity.ok(suggestion);
    }
}
