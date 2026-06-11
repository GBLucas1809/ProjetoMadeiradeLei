package com.madeiradelei.controller.dto;

public record EnderecoResponse(
    String logradouro,
    String numero,
    String cep,
    String cidade,
    String estado
) {}
