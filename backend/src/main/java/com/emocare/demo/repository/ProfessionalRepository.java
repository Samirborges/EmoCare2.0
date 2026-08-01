package com.emocare.demo.repository;

import com.emocare.demo.entity.Professional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProfessionalRepository extends JpaRepository<Professional, UUID> {



}
