package com.emocare.demo.repository;

import com.emocare.demo.entity.ConsultationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ConsultationTypeRepository extends JpaRepository<ConsultationType, UUID> {
    List<ConsultationType> findByProfessionalIdAndActiveTrue(UUID professionalId);
}
