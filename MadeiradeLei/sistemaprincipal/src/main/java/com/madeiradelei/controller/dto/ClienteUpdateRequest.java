package com.madeiradelei.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record ClienteUpdateRequest(
        @NotBlank(message = "A razão social é obrigatória para a atualização")
        String razaoSocial
) {
}