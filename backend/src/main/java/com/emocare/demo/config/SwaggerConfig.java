package com.emocare.demo.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.OAuthScope;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
        name = "oauth2Auth",
        type = SecuritySchemeType.OAUTH2,
        flows = @OAuthFlows(
                authorizationCode = @OAuthFlow(
                        authorizationUrl = "https://seu-autenticador.com",
                        tokenUrl = "https://seu-autenticador.com",
                        scopes = {
                                @OAuthScope(name = "read", description = "Acesso de leitura"),
                                @OAuthScope(name = "write", description = "Acesso de escrita")
                        }
                )
        )
)
public class SwaggerConfig {
    // Com esta configuração, o Swagger UI abrirá a janela de login do OAuth2
}
