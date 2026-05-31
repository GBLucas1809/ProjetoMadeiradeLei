package com.madeiradelei.controller.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record DimensoesRequest(
        @NotNull @DecimalMin("0.01") Double altura,
        @NotNull @DecimalMin("0.01") Double largura,
        @NotNull @DecimalMin("0.01") Double profundidade
) {}