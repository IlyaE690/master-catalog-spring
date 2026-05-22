package kfu.itis.model.dto;

import java.util.List;

public record RecommendedMasterDto(
        Long id,
        String fullName,
        Double rating,
        String phone,
        List<String> specializations,
        Integer completedOrdersCount
) {
}