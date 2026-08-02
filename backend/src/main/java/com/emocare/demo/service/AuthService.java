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
import com.emocare.demo.repository.ProfessionalRepository;
import com.emocare.demo.entity.Professional;
import com.emocare.demo.DTO.UpdateUserDTO;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository repository;
    private final UserMapper mapper;
    private final SupabaseAuthClient supabaseAuthClient;
    private final ProfessionalRepository professionalRepository;


    public AuthService(UserRepository repository, UserMapper mapper,
                       SupabaseAuthClient supabaseAuthClient, ProfessionalRepository professionalRepository) {
        this.mapper = mapper;
        this.repository = repository;
        this.supabaseAuthClient = supabaseAuthClient;
        this.professionalRepository = professionalRepository;
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

    public User updateUser(String subject, UpdateUserDTO dto) {
        UUID userId = UUID.fromString(subject);
        User user = repository.findById(userId)
                .orElseThrow(() -> new AuthException("Usuário não encontrado"));

        // Campos que qualquer usuário pode editar
        if (dto.fullName() != null) user.setFullName(dto.fullName());
        if (dto.photoUrl() != null) user.setPhotoUrl(dto.photoUrl());
        if (dto.birthDate() != null) user.setBirthDate(dto.birthDate());
        if (dto.phone() != null) user.setPhone(dto.phone());

        repository.save(user);

        // Campos específicos para profissionais
        if (user.getRole() == UserRole.PROFESSIONAL) {
            Professional professional = professionalRepository.findById(user.getId()).orElseGet(() -> {
                Professional p = new Professional();
                p.setId(user.getId());
                p.setUser(user);
                return p;
            });

            if (dto.crp() != null) professional.setCrp(dto.crp());
            if (dto.biography() != null) professional.setBiography(dto.biography());
            if (dto.therapeuticApproach() != null) professional.setTherapeuticApproach(dto.therapeuticApproach());
            if (dto.experienceYears() != null) professional.setExperienceYears(dto.experienceYears());

            professionalRepository.save(professional);
        }

        return user;
    }
}
