package com.emocare.demo.service;

import com.emocare.demo.DTO.UserResponseDTO;
import com.emocare.demo.entity.User;
import com.emocare.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {

    @Autowired
    private UserRepository repository;

    public List<UserResponseDTO> bringUsers() {

        ArrayList<User> usersEntity = repository.findAll();

        return usersEntity.stream()
                .map(u -> new UserResponseDTO(
                        u.getId(),
                        u.getFullName(),
                        u.getEmail(),
                        u.getRole()
                ))
                .collect(Collectors.toList());

    }

}
