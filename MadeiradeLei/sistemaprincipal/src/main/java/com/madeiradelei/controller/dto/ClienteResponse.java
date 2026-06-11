package com.madeiradelei.controller.dto;

import java.time.LocalDate;

public record ClienteResponse(
        String id,
        String cnpj,
        String razaoSocial,
        LocalDate dataCadastramento,
        EnderecoResponse enderecoCobranca
) {
}