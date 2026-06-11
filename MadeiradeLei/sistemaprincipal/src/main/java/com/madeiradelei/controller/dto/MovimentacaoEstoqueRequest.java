package com.madeiradelei.controller.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record MovimentacaoEstoqueRequest(
        @NotNull @Positive BigDecimal quantidade
) {}