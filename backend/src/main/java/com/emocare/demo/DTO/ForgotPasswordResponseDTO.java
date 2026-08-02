package com.emocare.demo.DTO;

public record ForgotPasswordResponseDTO(
        boolean canRecoverLocally,
        String message
) {
}
