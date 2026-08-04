package com.emocare.demo.service;

import com.emocare.demo.DTO.BlockedDateDTO;
import com.emocare.demo.DTO.CreateBlockedDateDTO;
import com.emocare.demo.entity.BlockedDate;
import com.emocare.demo.entity.Professional;
import com.emocare.demo.exception.ResourceNotFoundException;
import com.emocare.demo.repository.BlockedDateRepository;
import com.emocare.demo.repository.ProfessionalRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BlockedDateService {

    private final BlockedDateRepository repository;
    private final ProfessionalRepository professionalRepository;

    public BlockedDateService(BlockedDateRepository repository, ProfessionalRepository professionalRepository) {
        this.repository = repository;
        this.professionalRepository = professionalRepository;
    }

    public List<BlockedDateDTO> listMine(UUID professionalId) {
        return repository.findByProfessionalIdOrderByBlockedDateAsc(professionalId).stream()
                .map(BlockedDateDTO::from).toList();
    }

    @Transactional
    public BlockedDateDTO create(UUID professionalId, CreateBlockedDateDTO dto) {
        if (dto.fullDay() && (dto.startTime() != null || dto.endTime() != null)) {
            throw new IllegalArgumentException("Bloqueio de dia inteiro não deve informar horário");
        }
        if (!dto.fullDay()) {
            if (dto.startTime() == null || dto.endTime() == null) {
                throw new IllegalArgumentException("Informe início e fim, ou marque como dia inteiro");
            }
            if (!dto.startTime().isBefore(dto.endTime())) {
                throw new IllegalArgumentException("Hora de início deve ser antes da hora de término");
            }
        }

        Professional professional = professionalRepository.findById(professionalId)
                .orElseThrow(() -> new ResourceNotFoundException("Profissional não encontrado"));

        BlockedDate blocked = new BlockedDate();
        blocked.setProfessional(professional);
        blocked.setBlockedDate(dto.blockedDate());
        blocked.setIsFullDay(dto.fullDay());
        blocked.setStartTime(dto.fullDay() ? null : dto.startTime());
        blocked.setEndTime(dto.fullDay() ? null : dto.endTime());
        blocked.setReason(dto.reason());

        return BlockedDateDTO.from(repository.save(blocked));
    }

    @Transactional
    public void delete(UUID professionalId, UUID blockedDateId) {
        BlockedDate blocked = repository.findById(blockedDateId)
                .orElseThrow(() -> new ResourceNotFoundException("Bloqueio não encontrado"));
        if (!blocked.getProfessional().getId().equals(professionalId)) {
            throw new AccessDeniedException("Você não pode remover um bloqueio de outro profissional");
        }
        repository.delete(blocked);
    }
}
