package com.emocare.demo.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record CreateUserDTO(
        @NotBlank String fullName,

        @Email @NotBlank String email,

        @Size(min = 8, message = "...") @NotBlank String password,

        @NotBlank String phoneNumber,

        @NotNull
        @JsonFormat(pattern = "yyyy-MM-dd")
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate birthDate

) {

}
