package kfu.itis.service.impl;

import kfu.itis.model.entity.Specialization;
import kfu.itis.repository.SpecializationRepository;
import kfu.itis.service.SpecializationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class SpecializationServiceImpl implements SpecializationService {

    private final SpecializationRepository specializationRepository;

    public SpecializationServiceImpl(SpecializationRepository specializationRepository) {
        this.specializationRepository = specializationRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Specialization> findAll() {
        return specializationRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Specialization> findById(Long id) {
        return specializationRepository.findById(id);
    }

    @Override
    @Transactional
    public Specialization create(String name, String description, BigDecimal basePrice, Boolean weatherDependent) {
        Specialization specialization = new Specialization();
        specialization.setName(name);
        specialization.setDescription(description);
        specialization.setBasePrice(basePrice);
        specialization.setWeatherDependent(weatherDependent != null ? weatherDependent : false);
        return specializationRepository.save(specialization);
    }

    @Override
    @Transactional
    public Optional<Specialization> update(Long id, String name, String description, BigDecimal basePrice, Boolean weatherDependent) {
        return specializationRepository.findById(id).map(specialization -> {
            specialization.setName(name);
            specialization.setDescription(description);
            specialization.setBasePrice(basePrice);
            specialization.setWeatherDependent(weatherDependent != null ? weatherDependent : false);
            return specializationRepository.save(specialization);
        });
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        if (!specializationRepository.existsById(id)) {
            return false;
        }
        specializationRepository.deleteById(id);
        return true;
    }

}