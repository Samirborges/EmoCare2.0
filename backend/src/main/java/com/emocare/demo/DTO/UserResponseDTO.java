package com.emocare.demo.DTO;

import com.emocare.demo.entity.User;
import com.emocare.demo.entity.enums.AuthProvider;
import com.emocare.demo.entity.enums.UserRole;

import java.util.UUID;

public record UserResponseDTO(
        UUID id,
        String fullName,
        String email,
        UserRole role,
        AuthProvider provider
) {

    public static UserResponseDTO from(User user) {
        return new UserResponseDTO(user.getId(), user.getFullName(), user.getEmail(), user.getRole(), user.getProvider());
    }

}


