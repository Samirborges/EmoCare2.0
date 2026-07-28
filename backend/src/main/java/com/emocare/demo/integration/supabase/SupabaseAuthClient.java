package com.emocare.demo.integration.supabase;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.UUID;

@Component
public class SupabaseAuthClient {

    private final RestClient restClient;

    public SupabaseAuthClient(
            @Value("${supabase.url}") String supabaseUrl,
            @Value("${supabase.service-role-key}") String serviceRoleKey) {
        this.restClient = RestClient.builder()
                .baseUrl(supabaseUrl + "/auth/v1")
                .defaultHeader("apikey", serviceRoleKey)
                .defaultHeader("Authorization", "Bearer " + serviceRoleKey)
                .build();
    }

    public SupabaseUserResponse createUser(String email, String password) {
        return restClient.post()
                .uri("/admin/users")
                .body(new SupabaseCreateUserRequest(email, password, true))
                .retrieve()
                .body(SupabaseUserResponse.class);
    }

    public void deleteUser(UUID id) {
        restClient.delete()
                .uri("/admin/users/{id}", id)
                .retrieve()
                .toBodilessEntity();
    }

}
