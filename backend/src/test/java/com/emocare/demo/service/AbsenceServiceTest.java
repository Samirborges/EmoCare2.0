package com.emocare.demo.service;

import com.emocare.demo.DTO.CreateAbsenceDTO;
import com.emocare.demo.entity.Absence;
import com.emocare.demo.entity.Professional;
import com.emocare.demo.entity.enums.AbsenceStatus;
import com.emocare.demo.entity.enums.AbsenceType;
import com.emocare.demo.repository.AbsenceRepository;
import com.emocare.demo.repository.ProfessionalRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AbsenceServiceTest {

    @Mock
    AbsenceRepository repository;
    @Mock
    ProfessionalRepository professionalRepository;
    @Mock SystemSettingsService settings;

    @InjectMocks
    AbsenceService service;

    @Test
    void ferias_devemFicarPendentes_semExigirJustificativa() {
        UUID professionalId = UUID.randomUUID();
        when(professionalRepository.findById(professionalId)).thenReturn(Optional.of(new Professional()));
        when(repository.save(any(Absence.class))).thenAnswer(inv -> inv.getArgument(0));

        var dto = new CreateAbsenceDTO(AbsenceType.VACATION, LocalDate.now().plusDays(10), LocalDate.now().plusDays(20), null);
        var result = service.create(professionalId, dto);

        assertThat(result.status()).isEqualTo(AbsenceStatus.PENDING);
    }

    @Test
    void indisponibilidade_devidamenteAntecipadaEJustificada_ficaAprovadaAutomaticamente() {
        UUID professionalId = UUID.randomUUID();
        when(professionalRepository.findById(professionalId)).thenReturn(Optional.of(new Professional()));
        when(settings.getInt("min_advance_notice_absence_days", 3)).thenReturn(3);
        when(repository.save(any(Absence.class))).thenAnswer(inv -> inv.getArgument(0));

        var dto = new CreateAbsenceDTO(AbsenceType.TEMPORARY_UNAVAILABILITY,
                LocalDate.now().plusDays(5), LocalDate.now().plusDays(6), "Consulta médica");
        var result = service.create(professionalId, dto);

        assertThat(result.status()).isEqualTo(AbsenceStatus.APPROVED);
    }

    @Test
    void indisponibilidade_semJustificativa_develancarExcecao() {
        UUID professionalId = UUID.randomUUID();
        when(professionalRepository.findById(professionalId)).thenReturn(Optional.of(new Professional()));

        var dto = new CreateAbsenceDTO(AbsenceType.TEMPORARY_UNAVAILABILITY,
                LocalDate.now().plusDays(5), LocalDate.now().plusDays(6), null);

        assertThatThrownBy(() -> service.create(professionalId, dto)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void indisponibilidade_comPoucaAntecedencia_develancarExcecao() {
        UUID professionalId = UUID.randomUUID();
        when(professionalRepository.findById(professionalId)).thenReturn(Optional.of(new Professional()));
        when(settings.getInt("min_advance_notice_absence_days", 3)).thenReturn(3);

        var dto = new CreateAbsenceDTO(AbsenceType.TEMPORARY_UNAVAILABILITY,
                LocalDate.now().plusDays(1), LocalDate.now().plusDays(2), "Urgência");

        assertThatThrownBy(() -> service.create(professionalId, dto)).isInstanceOf(IllegalArgumentException.class);
    }
}
