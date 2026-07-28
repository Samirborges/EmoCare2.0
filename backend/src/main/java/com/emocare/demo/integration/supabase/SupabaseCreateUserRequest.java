package com.emocare.demo.integration.supabase;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SupabaseCreateUserRequest(
        String email,
        String password,
        @JsonProperty("email_confirm") boolean emailConfirm
) {
}
