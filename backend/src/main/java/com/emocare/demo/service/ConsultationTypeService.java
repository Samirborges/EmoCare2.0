package com.emocare.demo.service;

import com.emocare.demo.DTO.ConsultationTypeDTO;
import com.emocare.demo.DTO.CreateConsultationTypeDTO;
import com.emocare.demo.entity.ConsultationType;
import com.emocare.demo.entity.Professional;
import com.emocare.demo.exception.ResourceNotFoundException;
import com.emocare.demo.repository.ConsultationTypeRepository;
import com.emocare.demo.repository.ProfessionalRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ConsultationTypeService {

    private final ConsultationTypeRepository repository;
    private final ProfessionalRepository professionalRepository;

    public ConsultationTypeService(ConsultationTypeRepository repository, ProfessionalRepository professionalRepository) {
        this.repository = repository;
        this.professionalRepository = professionalRepository;
    }

    public List<ConsultationTypeDTO> listMine(UUID professionalId) {
        return repository.findByProfessionalIdAndActiveTrue(professionalId).stream()
                .map(ConsultationTypeDTO::from).toList();
    }

    @Transactional
    public ConsultationTypeDTO create(UUID professionalId, CreateConsultationTypeDTO dto) {
        Professional professional = professionalRepository.findById(professionalId)
                .orElseThrow(() -> new ResourceNotFoundException("Profissional não encontrado"));

        ConsultationType type = new ConsultationType();
        type.setProfessional(professional);
        type.setName(dto.name());
        type.setDurationMinutes(dto.durationMinutes());
        type.setPrice(dto.price());

        return ConsultationTypeDTO.from(repository.save(type));
    }

    @Transactional
    public ConsultationTypeDTO update(UUID professionalId, UUID typeId, CreateConsultationTypeDTO dto) {
        ConsultationType type = findOwned(professionalId, typeId);
        type.setName(dto.name());
        type.setDurationMinutes(dto.durationMinutes());
        type.setPrice(dto.price());
        return ConsultationTypeDTO.from(repository.save(type));
    }

    @Transactional
    public void deactivate(UUID professionalId, UUID typeId) {
        ConsultationType type = findOwned(professionalId, typeId);
        type.setActive(false); // soft delete — appointments.consultation_type_id tem ON DELETE RESTRICT, um delete de verdade quebraria histórico
        repository.save(type);
    }

    private ConsultationType findOwned(UUID professionalId, UUID typeId) {
        ConsultationType type = repository.findById(typeId)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de consulta não encontrado"));
        if (!type.getProfessional().getId().equals(professionalId)) {
            throw new AccessDeniedException("Você não pode alterar um tipo de consulta de outro profissional");
        }
        return type;
    }

}
