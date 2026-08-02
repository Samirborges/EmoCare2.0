package com.emocare.demo.service;

import com.emocare.demo.DTO.CreateUserDTO;
import com.emocare.demo.DTO.ForgotPasswordResponseDTO;
import com.emocare.demo.entity.User;
import com.emocare.demo.entity.enums.AuthProvider;
import com.emocare.demo.entity.enums.UserRole;
import com.emocare.demo.exception.AuthException;
import com.emocare.demo.integration.supabase.SupabaseAuthClient;
import com.emocare.demo.integration.supabase.SupabaseUserResponse;
import com.emocare.demo.mapper.UserMapper;
import com.emocare.demo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
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

    public ForgotPasswordResponseDTO checkPasswordRecoveryEligibility(String email) {
        Optional<User> userOpt = repository.findByEmail(email);

        if(userOpt.isEmpty()) {
            return new ForgotPasswordResponseDTO(true, "Se este e-mail estiver cadastrado, você receberá um link de recuperação.");
        }

        User user = userOpt.get();
        if (user.getProvider() == AuthProvider.GOOGLE) {
            return new ForgotPasswordResponseDTO(false, "Esta conta usa login via Google. Recupere sua senha diretamente com o Google.");
        }

        return new ForgotPasswordResponseDTO(true, "Se ese e-mail estiver cadastrado, você receberá um link de recuperação.");
    }
}
