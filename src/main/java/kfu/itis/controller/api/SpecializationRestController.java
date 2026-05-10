package kfu.itis.controller.api;

import kfu.itis.api.generated.dto.CreateSpecializationRequest;
import kfu.itis.api.generated.dto.SpecializationResponse;
import kfu.itis.api.generated.dto.UpdateSpecializationRequest;
import kfu.itis.model.entity.Specialization;
import kfu.itis.service.SpecializationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/specializations")
public class SpecializationRestController {

    private final SpecializationService specializationService;

    public SpecializationRestController(SpecializationService specializationService) {
        this.specializationService = specializationService;
    }

    @GetMapping
    public ResponseEntity<List<SpecializationResponse>> getAll() {
        List<SpecializationResponse> list = specializationService.findAll()
                .stream()
                .map(this::toDto)
                .toList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SpecializationResponse> getById(@PathVariable Long id) {
        return specializationService.findById(id)
                .map(this::toDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<SpecializationResponse> create(@RequestBody CreateSpecializationRequest request) {
        BigDecimal price = request.getBasePrice() != null
                ? request.getBasePrice()
                : BigDecimal.ZERO;

        Specialization created = specializationService.create(
                request.getName(),
                request.getDescription(),
                price,
                request.getWeatherDependent()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SpecializationResponse> update(@PathVariable Long id,
                                                         @RequestBody UpdateSpecializationRequest request) {
        BigDecimal price = request.getBasePrice() != null
                ? request.getBasePrice()
                : BigDecimal.ZERO;

        return specializationService.update(
                        id,
                        request.getName(),
                        request.getDescription(),
                        price,
                        request.getWeatherDependent()
                )
                .map(this::toDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return specializationService.delete(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    private SpecializationResponse toDto(Specialization entity) {
        SpecializationResponse response = new SpecializationResponse();
        response.setId(entity.getId());
        response.setName(entity.getName());
        response.setDescription(entity.getDescription());
        response.setBasePrice(entity.getBasePrice() != null
                ? entity.getBasePrice().doubleValue()
                : null);
        response.setWeatherDependent(entity.getWeatherDependent());
        return response;
    }
}
