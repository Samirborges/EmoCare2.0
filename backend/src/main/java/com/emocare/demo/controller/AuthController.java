package com.emocare.demo.controller;

import com.emocare.demo.DTO.CreateUserDTO;
import com.emocare.demo.DTO.UserResponseDTO;
import com.emocare.demo.entity.User;
import com.emocare.demo.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(@Valid @RequestBody CreateUserDTO userDTO) {

        authService.register(userDTO);

        return ResponseEntity.status(HttpStatus.CREATED).build();

    }


    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> me(@AuthenticationPrincipal Jwt jwt) {
        User user = authService.getAuthenticatedUser(jwt.getSubject());
        return ResponseEntity.ok(UserResponseDTO.from(user));
    }

}
