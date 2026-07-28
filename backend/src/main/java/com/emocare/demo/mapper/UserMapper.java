package com.emocare.demo.mapper;

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

}
