package com.emocare.demo.service;

import com.emocare.demo.DTO.CreateUserDTO;
import com.emocare.demo.entity.User;
import com.emocare.demo.entity.enums.UserRole;
import com.emocare.demo.exception.AuthException;
import com.emocare.demo.integration.supabase.SupabaseAuthClient;
import com.emocare.demo.integration.supabase.SupabaseUserResponse;
import com.emocare.demo.mapper.UserMapper;
import com.emocare.demo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository repository;
    private final UserMapper mapper;
    private final SupabaseAuthClient supabaseAuthClient;


    public AuthService(UserRepository repository, UserMapper mapper,
                       SupabaseAuthClient supabaseAuthClient) {
        this.mapper = mapper;
        this.repository = repository;
        this.supabaseAuthClient = supabaseAuthClient;
    }

    public User register(CreateUserDTO userDTO) {
        if (repository.existsByEmail(userDTO.email())) {
            throw new AuthException("E-mail já existe");
        }

        SupabaseUserResponse supabaseUser =
                supabaseAuthClient.createUser(userDTO.email(), userDTO.password());

        try {
            User user = mapper.toEntity(userDTO);
            user.setId(supabaseUser.id());
            user.setRole(UserRole.PATIENT);
            return repository.save(user);
        } catch (Exception e) {
            supabaseAuthClient.deleteUser(supabaseUser.id());
            throw new AuthException("Erro ao criar o perfil do usuário", e);
        }

    }

    public User getAuthenticatedUser(String subject) {
        UUID userId = UUID.fromString(subject);
        return repository.findById(userId)
                .orElseThrow(() -> new AuthException("Usuário não encontrado"));
    }
}
