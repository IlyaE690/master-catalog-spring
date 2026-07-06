package kfu.itis.service.impl;

import kfu.itis.model.dto.AiSuggestionResponseDto;
import kfu.itis.model.dto.RecommendedMasterDto;
import kfu.itis.model.entity.Specialization;
import kfu.itis.model.entity.User;
import kfu.itis.service.AiOrderSuggestionService;
import kfu.itis.service.OrderService;
import kfu.itis.service.SpecializationService;
import kfu.itis.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AiOrderSuggestionServiceImpl implements AiOrderSuggestionService {

    private final UserService userService;
    private final SpecializationService specializationService;
    private final OrderService orderService;

    public AiOrderSuggestionServiceImpl(UserService userService,
                                        SpecializationService specializationService,
                                        OrderService orderService) {
        this.userService = userService;
        this.specializationService = specializationService;
        this.orderService = orderService;
    }

    @Override
    @Transactional(readOnly = true)
    public AiSuggestionResponseDto buildSuggestion(String issueDescription, Double minRating) {
        Specialization specialization = inferSpecializationByDescription(issueDescription)
                .orElseThrow(() -> new RuntimeException("Не удалось определить специализацию по описанию"));
        return buildSuggestion(issueDescription, specialization.getId(), minRating);
    }

    @Override
    @Transactional(readOnly = true)
    public AiSuggestionResponseDto buildSuggestion(String issueDescription, Long specializationId, Double minRating) {
        Specialization specialization = specializationService.findById(specializationId)
                .orElseThrow(() -> new RuntimeException("Специализация не найдена"));

        List<User> masters = minRating == null
                ? userService.findAllMastersBySpecialization(specialization)
                : userService.findMastersBySpecializationAndMinRating(specialization, minRating);

        List<RecommendedMasterDto> recommendedMasters = masters.stream()
                .limit(5)
                .map(master -> new RecommendedMasterDto(
                        master.getId(),
                        (master.getFirstName() != null ? master.getFirstName() : "") + " " +
                                (master.getLastName() != null ? master.getLastName() : ""),
                        master.getRating(),
                        master.getPhone(),
                        master.getSpecializations().stream()
                                .map(Specialization::getName)
                                .collect(Collectors.toList()),
                        (int) orderService.countCompletedByMaster(master)
                ))
                .toList();

        return new AiSuggestionResponseDto(
                specialization.getId(),
                specialization.getName(),
                recommendedMasters
        );
    }

    private Optional<Specialization> inferSpecializationByDescription(String issueDescription) {
        String normalized = issueDescription == null ? "" : issueDescription.toLowerCase();
        Map<String, List<String>> keywordsBySpecialization = Map.of(
                "Сантехник", List.of("теч", "кран", "труб", "слив", "унитаз", "вода", "батаре"),
                "Электрик", List.of("свет", "провод", "розет", "электр", "выбивает", "щиток"),
                "Отделочник", List.of("обои", "штукатур", "краск", "плитк", "потол", "стен")
        );

        List<Specialization> allSpecializations = specializationService.findAll();
        return allSpecializations.stream()
                .map(spec -> Map.entry(spec, scoreSpecialization(normalized, keywordsBySpecialization.get(spec.getName()))))
                .filter(entry -> entry.getValue() > 0)
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .or(() -> allSpecializations.stream().findFirst());
    }

    private int scoreSpecialization(String normalizedIssueDescription, List<String> keywords) {
        if (keywords == null || normalizedIssueDescription.isBlank()) {
            return 0;
        }
        return (int) keywords.stream()
                .filter(normalizedIssueDescription::contains)
                .count();
    }
}