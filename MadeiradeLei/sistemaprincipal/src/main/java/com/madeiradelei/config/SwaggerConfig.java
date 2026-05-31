package com.madeiradelei.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        // 1. Define o nome do nosso esquema de segurança
        final String securitySchemeName = "bearer-jwt";

        return new OpenAPI()
                // 2. Configura as informações gerais da página
                .info(new Info()
                        .title("API - Fábrica Madeira de Lei")
                        .description("Documentação interativa com Springdoc OpenAPI e JWT")
                        .version("1.0.0"))
                
                // 3. Diz ao Swagger que todas as rotas exigem este token por padrão
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                
                // 4. Constrói o botão "Authorize" configurado para o padrão Bearer JWT
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }
}