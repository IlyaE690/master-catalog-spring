package kfu.itis.service;

import kfu.itis.entity.Specialization;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface SpecializationService {

    List<Specialization> findAll();

    Optional<Specialization> findById(Long id);

    Specialization create(String name, String description, BigDecimal basePrice, Boolean weatherDependent);

    Optional<Specialization> update(Long id, String name, String description, BigDecimal basePrice, Boolean weatherDependent);

    boolean delete(Long id);

    boolean existsByName(String name);
}