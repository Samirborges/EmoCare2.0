package com.emocare.demo.DTO;

import com.emocare.demo.entity.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record AdminCreateUserAdminRequestDTO(
        @NotBlank
        String fullName,

        @Email
        @NotBlank
        String email,

        @Size(min = 8, message = "A senha deve ter ao menos 8 caracteres")
        @NotBlank
        String password,

        String phoneNumber,

        @NotNull
        UserRole role,

        @NotNull
        @JsonFormat(pattern = "yyyy-MM-dd")
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate birthDate
) {
}
