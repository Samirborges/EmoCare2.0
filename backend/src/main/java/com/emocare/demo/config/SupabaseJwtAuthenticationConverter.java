package com.emocare.demo.config;

import com.emocare.demo.entity.User;
import com.emocare.demo.entity.enums.UserRole;
import com.emocare.demo.repository.UserRepository;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class SupabaseJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final UserRepository userRepository;

    public SupabaseJwtAuthenticationConverter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        UUID userId = UUID.fromString(jwt.getSubject());

        UserRole role = userRepository.findById(userId)
                .map(User::getRole)
                .orElseThrow(() -> new BadCredentialsException("Usuário autenticado não encotrado localmente"));

        var authority = new SimpleGrantedAuthority("ROLE_" + role.name());
        return new JwtAuthenticationToken(jwt, List.of(authority), jwt.getSubject());
    }

}
