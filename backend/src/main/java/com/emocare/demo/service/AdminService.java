package com.emocare.demo.service;

import com.emocare.demo.DTO.AdminCreateUserDTO;
import com.emocare.demo.DTO.UserResponseDTO;
import com.emocare.demo.entity.Professional;
import com.emocare.demo.entity.User;
import com.emocare.demo.entity.enums.UserRole;
import com.emocare.demo.exception.AuthException;
import com.emocare.demo.integration.supabase.SupabaseAuthClient;
import com.emocare.demo.integration.supabase.SupabaseUserResponse;
import com.emocare.demo.mapper.UserMapper;
import com.emocare.demo.repository.ProfessionalRepository;
import com.emocare.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfessionalRepository professionalRepository;

    @Autowired
    private SupabaseAuthClient supabaseAuthClient;

    @Autowired
    private UserMapper mapper;

    public List<UserResponseDTO> bringUsers() {
        return userRepository.findAll().stream()
                .map(UserResponseDTO::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public User createUser(AdminCreateUserDTO userDTO) {
        if (userRepository.existsByEmail(userDTO.email())) {
            throw new AuthException("E-mail já existe");
        }

        if (userDTO.role() == UserRole.PROFESSIONAL && (userDTO.crp() == null || userDTO.crp().isBlank())) {
            throw new AuthException("CRP é obrigatório para cadastro de profissional");
        }

        SupabaseUserResponse supabaseUser =
                supabaseAuthClient.createUser(userDTO.email(), userDTO.password());

        try {
            User user = mapper.toEntity(userDTO);
            user.setId(supabaseUser.id());
            User savedUser = userRepository.save(user);

            if (savedUser.getRole() == UserRole.PROFESSIONAL) {
                Professional professional = new Professional();
                professional.setUser(savedUser);
                professional.setCrp(userDTO.crp());
                professional.setBiography(userDTO.biography());
                professional.setTherapeuticApproach(userDTO.therapeuticApproach());
                professional.setExperienceYears(userDTO.experienceYears());
                professionalRepository.save(professional);
            }

            return savedUser;

        } catch (Exception e) {
            supabaseAuthClient.deleteUser(supabaseUser.id());
            throw new AuthException("Erro ao criar o perfil do usuário", e);
        }


    }

}
