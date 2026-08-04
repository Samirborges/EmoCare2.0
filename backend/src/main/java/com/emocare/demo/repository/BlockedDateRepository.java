package com.emocare.demo.repository;

import com.emocare.demo.entity.BlockedDate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BlockedDateRepository extends JpaRepository<BlockedDate, UUID> {
    List<BlockedDate> findByProfessionalIdOrderByBlockedDateAsc(UUID professionalId);
}
