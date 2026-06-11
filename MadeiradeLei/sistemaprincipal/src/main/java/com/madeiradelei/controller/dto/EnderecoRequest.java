package com.madeiradelei.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record EnderecoRequest(
        @NotBlank(message = "O logradouro é obrigatório")
        String logradouro,
        
        @NotBlank(message = "O número é obrigatório")
        String numero,
        
        @NotBlank(message = "O CEP é obrigatório")
        @Pattern(regexp = "\\d{8}", message = "O CEP deve conter 8 dígitos")
        String cep,
        
        @NotBlank(message = "A cidade é obrigatória")
        String cidade,
        
        @NotBlank(message = "O estado é obrigatório")
        String estado
) {
}