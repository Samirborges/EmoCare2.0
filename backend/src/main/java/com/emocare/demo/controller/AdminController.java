package com.emocare.demo.controller;

import com.emocare.demo.DTO.UserResponseDTO;
import com.emocare.demo.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService service;

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponseDTO>> getUsers(){
        return ResponseEntity.ok(service.bringUsers());
    }
}
