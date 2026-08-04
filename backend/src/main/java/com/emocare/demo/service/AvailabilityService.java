package com.emocare.demo.service;

import com.emocare.demo.DTO.AvailabilitySlotDTO;
import com.emocare.demo.DTO.CreateAvailabilitySlotDTO;
import com.emocare.demo.entity.Professional;
import com.emocare.demo.entity.ProfessionalAvailability;
import com.emocare.demo.exception.ResourceNotFoundException;
import com.emocare.demo.repository.ProfessionalAvailabilityRepository;
import com.emocare.demo.repository.ProfessionalRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class AvailabilityService {

    private final ProfessionalAvailabilityRepository repository;
    private final ProfessionalRepository professionalRepository;

    public AvailabilityService(ProfessionalAvailabilityRepository repository, ProfessionalRepository professionalRepository) {
        this.repository = repository;
        this.professionalRepository = professionalRepository;
    }

    public List<AvailabilitySlotDTO> listMine(UUID professionalId) {
        return repository.findByProfessionalIdAndActiveTrueOrderByWeekdayAscStartTimeAsc(professionalId).stream()
                .map(AvailabilitySlotDTO::from).toList();
    }

    @Transactional
    public AvailabilitySlotDTO create(UUID professionalId, CreateAvailabilitySlotDTO dto) {
        if (!dto.startTime().isBefore(dto.endTime())) {
            throw new IllegalArgumentException("O horário de início deve ser antes do horário de término");
        }

        Professional professional = professionalRepository.findById(professionalId)
                .orElseThrow(() -> new ResourceNotFoundException("Profissional não encontrado"));

        ProfessionalAvailability slot = new ProfessionalAvailability();
        slot.setProfessional(professional);
        slot.setWeekday(dto.weekday());
        slot.setStartTime(dto.startTime());
        slot.setEndTime(dto.endTime());

        return AvailabilitySlotDTO.from(repository.save(slot));
    }

    @Transactional
    public void deactivate(UUID professionalId, UUID slotId) {
        ProfessionalAvailability slot = repository.findById(slotId)
                .orElseThrow(() -> new ResourceNotFoundException("Horário não encontrado"));
        if (!slot.getProfessional().getId().equals(professionalId)) {
            throw new AccessDeniedException("Você não pode remover um horário de outro profissional");
        }
        slot.setActive(false);
        repository.save(slot);
    }

    @Transactional
    public void updateBuffer(UUID professionalId, Short bufferMinutes) {
        Professional professional = professionalRepository.findById(professionalId)
                .orElseThrow(() -> new ResourceNotFoundException("Profissional não encontrado"));
        professional.setBufferMinutes(bufferMinutes);
        professionalRepository.save(professional);
    }
}
