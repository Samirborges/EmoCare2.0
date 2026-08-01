package com.emocare.demo.repository;

import com.emocare.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    boolean existsByEmail(String email);
    ArrayList<User> findAll();
}
