package com.emocare.demo.service;

import com.emocare.demo.DTO.AbsenceDTO;
import com.emocare.demo.DTO.CreateAbsenceDTO;
import com.emocare.demo.entity.Absence;
import com.emocare.demo.entity.Professional;
import com.emocare.demo.entity.enums.AbsenceStatus;
import com.emocare.demo.entity.enums.AbsenceType;
import com.emocare.demo.exception.ResourceNotFoundException;
import com.emocare.demo.repository.AbsenceRepository;
import com.emocare.demo.repository.ProfessionalRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class AbsenceService {

    private final AbsenceRepository repository;
    private final ProfessionalRepository professionalRepository;
    private final SystemSettingsService settings;

    public AbsenceService(AbsenceRepository repository, ProfessionalRepository professionalRepository, SystemSettingsService settings) {
        this.repository = repository;
        this.professionalRepository = professionalRepository;
        this.settings = settings;
    }

    public List<AbsenceDTO> listMine(UUID professionalId) {
        return repository.findByProfessionalIdOrderByStartDateDesc(professionalId).stream()
                .map(AbsenceDTO::from).toList();
    }

    @Transactional
    public AbsenceDTO create(UUID professionalId, CreateAbsenceDTO dto) {
        if (dto.endDate().isBefore(dto.startDate())) {
            throw new IllegalArgumentException("Data de término não pode ser antes da data de início");
        }

        Professional professional = professionalRepository.findById(professionalId)
                .orElseThrow(() -> new ResourceNotFoundException("Profissional não encontrado"));

        Absence absence = new Absence();
        absence.setProfessional(professional);
        absence.setType(dto.type());
        absence.setStartDate(dto.startDate());
        absence.setEndDate(dto.endDate());
        absence.setReason(dto.reason());

        if (dto.type() == AbsenceType.TEMPORARY_UNAVAILABILITY) {
            if (dto.reason() == null || dto.reason().isBlank()) {
                throw new IllegalArgumentException("Justificativa é obrigatória para indisponibilidade temporária");
            }
            int minAdvanceDays = settings.getInt("min_advance_notice_absence_days", 3);
            if (dto.startDate().isBefore(LocalDate.now().plusDays(minAdvanceDays))) {
                throw new IllegalArgumentException(
                        "Indisponibilidade deve ser solicitada com ao menos " + minAdvanceDays + " dias de antecedência");
            }
            absence.setStatus(AbsenceStatus.APPROVED); // sem aprovação — só as duas regras acima
        } else {
            absence.setStatus(AbsenceStatus.PENDING); // férias aguardam admin
        }

        return AbsenceDTO.from(repository.save(absence));
    }

    public List<AbsenceDTO> listPendingVacations() {
        return repository.findByTypeAndStatus(AbsenceType.VACATION, AbsenceStatus.PENDING).stream()
                .map(AbsenceDTO::from).toList();
    }

    @Transactional
    public AbsenceDTO reviewVacation(UUID absenceId, UUID adminId, boolean approve, String notes) {
        Absence absence = repository.findById(absenceId)
                .orElseThrow(() -> new ResourceNotFoundException("Solicitação não encontrada"));
        if (absence.getType() != AbsenceType.VACATION) {
            throw new IllegalArgumentException("Apenas solicitações de férias passam por aprovação");
        }
        if (absence.getStatus() != AbsenceStatus.PENDING) {
            throw new IllegalArgumentException("Esta solicitação já foi avaliada");
        }

        absence.setStatus(approve ? AbsenceStatus.APPROVED : AbsenceStatus.REJECTED);
        absence.setReviewedBy(adminId);
        absence.setReviewedAt(OffsetDateTime.now());
        absence.setReviewNotes(notes);

        return AbsenceDTO.from(repository.save(absence));
    }
}
