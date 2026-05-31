package com.madeiradelei.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegistroUsuarioRequest(
        @NotBlank(message = "O e-mail é obrigatório") 
        @Email(message = "Formato de e-mail inválido") 
        String email,
        
        @NotBlank(message = "A senha é obrigatória") 
        String senha
) {}