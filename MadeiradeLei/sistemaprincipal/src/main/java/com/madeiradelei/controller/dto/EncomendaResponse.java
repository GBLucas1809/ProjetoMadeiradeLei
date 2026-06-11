package com.madeiradelei.controller.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record EncomendaResponse(
        String id,
        Long numero,
        LocalDate dataSolicitacao,
        BigDecimal valorTotal,
        BigDecimal valorDesconto,
        BigDecimal valorLiquido
) {
}