package com.madeiradelei.exception;

import com.madeiradelei.controller.dto.ErroPadrao;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class TratadorDeErros {

    // 1. Trata os erros de Regra de Negócio (ex: ResponseStatusException com 404 ou 400)
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErroPadrao> tratarErroDeRegraDeNegocio(ResponseStatusException ex, HttpServletRequest request) {
        ErroPadrao erro = new ErroPadrao(
                LocalDateTime.now(),
                ex.getStatusCode().value(),
                ex.getStatusCode().toString(),
                ex.getReason(),
                request.getRequestURI()
        );
        return ResponseEntity.status(ex.getStatusCode()).body(erro);
    }

    // 2. Trata os erros de Validação dos DTOs (ex: @NotBlank, @Email falharam)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroPadrao> tratarErroDeValidacao(MethodArgumentNotValidException ex, HttpServletRequest request) {
        
        // Pega todos os campos que falharam na validação e junta numa única mensagem de texto
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        String camposComErro = fieldErrors.stream()
                .map(f -> f.getField() + ": " + f.getDefaultMessage())
                .collect(Collectors.joining(" | "));

        ErroPadrao erro = new ErroPadrao(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request - Erro de Validação",
                camposComErro,
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    // 3. Trata os erros de Autorização do Spring Security (Erro 403 - @PreAuthorize falhou)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErroPadrao> tratarAcessoNegado(AccessDeniedException ex, HttpServletRequest request) {
        ErroPadrao erro = new ErroPadrao(
                LocalDateTime.now(),
                HttpStatus.FORBIDDEN.value(),
                "Forbidden",
                "Acesso negado: O seu perfil não tem permissão para executar esta ação.",
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(erro);
    }

    // 4. Captura qualquer outro erro genérico (Erro 500) para evitar expor a StackTrace do Java
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroPadrao> tratarErrosGenericos(Exception ex, HttpServletRequest request) {
        ErroPadrao erro = new ErroPadrao(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "Ocorreu um erro interno inesperado no servidor.",
                request.getRequestURI()
        );
        // Em produção, você guardaria o 'ex.getMessage()' num log (ex: SLF4J), mas não devolveria ao cliente
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
    }
}