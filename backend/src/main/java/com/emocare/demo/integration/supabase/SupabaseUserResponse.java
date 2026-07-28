package com.emocare.demo.integration.supabase;

import java.util.UUID;

public record SupabaseUserResponse(UUID id, String email) {
}
