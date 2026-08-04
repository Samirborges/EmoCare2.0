package com.emocare.demo.repository;

import com.emocare.demo.entity.Absence;
import com.emocare.demo.entity.enums.AbsenceStatus;
import com.emocare.demo.entity.enums.AbsenceType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AbsenceRepository extends JpaRepository<Absence, UUID> {
    List<Absence> findByProfessionalIdOrderByStartDateDesc(UUID professionalId);
    List<Absence> findByTypeAndStatus(AbsenceType type, AbsenceStatus status);

}
