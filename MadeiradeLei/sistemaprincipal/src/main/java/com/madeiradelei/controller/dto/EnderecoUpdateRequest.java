package com.madeiradelei.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record EnderecoUpdateRequest(
        @NotBlank(message = "O CEP é obrigatório") String cep,
        @NotBlank(message = "O logradouro é obrigatório") String logradouro,
        @NotBlank(message = "O número é obrigatório") String numero,
        @NotBlank(message = "A cidade é obrigatória") String cidade,
        @NotBlank(message = "O estado é obrigatório") String estado
) {}