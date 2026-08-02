package com.emocare.demo.service;

import com.emocare.demo.DTO.SpecialtyDTO;
import com.emocare.demo.entity.Professional;
import com.emocare.demo.entity.Specialty;
import com.emocare.demo.exception.ResourceNotFoundException;
import com.emocare.demo.repository.ProfessionalRepository;
import com.emocare.demo.repository.SpecialtyRepository;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Service
public class SpecialtyService {

    private final SpecialtyRepository specialtyRepository;
    private final ProfessionalRepository professionalRepository;

    public SpecialtyService(SpecialtyRepository specialtyRepository, ProfessionalRepository professionalRepository) {
        this.specialtyRepository = specialtyRepository;
        this.professionalRepository = professionalRepository;
    }

    public List<SpecialtyDTO> listActive() {
        return specialtyRepository.findByActiveTrue().stream()
                .map(SpecialtyDTO::from)
                .toList();
    }

    @Transactional
    public List<SpecialtyDTO> updateProfessionalSpecialties(UUID professionalId, List<UUID> specialtyIds) {
        Professional professional = professionalRepository.findById(professionalId)
                .orElseThrow(() -> new ResourceNotFoundException("Profissional não encontrado"));

        List<Specialty> found = specialtyRepository.findAllById(specialtyIds);
        if(found.size() != specialtyIds.size()) {
            throw new IllegalArgumentException("Uma ou mais especialidades informadas não existem");
        }

        professional.setSpecialties(new HashSet<>(found));
        professionalRepository.save(professional);

        return found.stream().map(SpecialtyDTO::from).toList();

    }
}
