package com.madeiradelei.controller.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

// O uso de 'record' garante imutabilidade e reduz boilerplate (getters, equals, hashcode implícitos)
public record ClienteRequest(
        
        @NotBlank(message = "O CNPJ é obrigatório")
        @Pattern(regexp = "\\d{14}", message = "O CNPJ deve conter exatamente 14 dígitos numéricos")
        String cnpj,
        
        @NotBlank(message = "A razão social é obrigatória")
        String razaoSocial,
        
        @NotNull(message = "O endereço de cobrança é obrigatório")
        @Valid // Instruí o Spring a validar os campos internos do EnderecoRequest
        EnderecoRequest enderecoCobranca
) {
}