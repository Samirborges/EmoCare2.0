package com.emocare.demo.mapper;

import com.emocare.demo.DTO.AdminCreateUserAdminRequestDTO;
import com.emocare.demo.DTO.AdminCreateUserProfessionalRequestDTO;
import com.emocare.demo.DTO.CreateUserDTO;
import com.emocare.demo.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(CreateUserDTO request) {
        User user = new User();

        user.setFullName(request.fullName());
        user.setEmail(request.email());
        user.setPhone(request.phoneNumber());
        user.setBirthDate(request.birthDate());

        return user;

    }

    public User toEntity(AdminCreateUserProfessionalRequestDTO request) {
        User user = new User();

        user.setFullName(request.fullName());
        user.setEmail(request.email());
        user.setRole(request.role());
        user.setPhone(request.phoneNumber());
        user.setBirthDate(request.birthDate());

        return user;

    }

    public User toEntity(AdminCreateUserAdminRequestDTO request) {
        User user = new User();

        user.setFullName(request.fullName());
        user.setEmail(request.email());
        user.setPhone(request.phoneNumber());
        user.setRole(request.role());
        user.setBirthDate(request.birthDate());

        return user;
    }

}
