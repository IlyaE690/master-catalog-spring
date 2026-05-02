package kfu.itis.controller;

import kfu.itis.api.generated.api.SpecializationApi;
import kfu.itis.api.generated.dto.CreateSpecializationRequest;
import kfu.itis.api.generated.dto.SpecializationResponse;
import kfu.itis.api.generated.dto.UpdateSpecializationRequest;
import kfu.itis.entity.Specialization;
import kfu.itis.service.SpecializationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
public class SpecializationController implements SpecializationApi {

    private final SpecializationService specializationService;

    public SpecializationController(SpecializationService specializationService) {
        this.specializationService = specializationService;
    }

    @Override
    public ResponseEntity<List<SpecializationResponse>> getAllSpecializations() {
        List<SpecializationResponse> specializations = specializationService.findAll()
                .stream()
                .map(this::toDto)
                .toList();
        return ResponseEntity.ok(specializations);
    }

    @Override
    public ResponseEntity<SpecializationResponse> getSpecializationById(Long id) {
        return specializationService.findById(id)
                .map(this::toDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<SpecializationResponse> createSpecialization(CreateSpecializationRequest request) {
        Specialization created = specializationService.create(
                request.getName(),
                request.getDescription(),
                request.getBasePrice() != null ? BigDecimal.valueOf(request.getBasePrice()) : null,
                request.getWeatherDependent()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(created));
    }

    @Override
    public ResponseEntity<SpecializationResponse> updateSpecialization(Long id, UpdateSpecializationRequest request) {
        return specializationService.update(
                        id,
                        request.getName(),
                        request.getDescription(),
                        request.getBasePrice() != null ? BigDecimal.valueOf(request.getBasePrice()) : null,
                        request.getWeatherDependent()
                )
                .map(this::toDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<Void> deleteSpecialization(Long id) {
        return specializationService.delete(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    private SpecializationResponse toDto(Specialization entity) {
        SpecializationResponse response = new SpecializationResponse();
        response.setId(entity.getId());
        response.setName(entity.getName());
        response.setDescription(entity.getDescription());
        response.setBasePrice(entity.getBasePrice() != null ? entity.getBasePrice().doubleValue() : null);
        response.setWeatherDependent(entity.getWeatherDependent());
        return response;
    }
}