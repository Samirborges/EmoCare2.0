package com.emocare.demo.repository;

import com.emocare.demo.entity.ProfessionalAvailability;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProfessionalAvailabilityRepository extends JpaRepository<ProfessionalAvailability, UUID> {
    List<ProfessionalAvailability> findByProfessionalIdAndActiveTrueOrderByWeekdayAscStartTimeAsc(UUID professionalId);
}
