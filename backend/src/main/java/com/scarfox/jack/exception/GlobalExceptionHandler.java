package com.scarfox.jack.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ProblemDetail tratarRecursoNaoEncontrado(
            RecursoNaoEncontradoException exception) {

        ProblemDetail problema =
                ProblemDetail.forStatus(HttpStatus.NOT_FOUND);

        problema.setTitle("Recurso não encontrado");
        problema.setDetail(exception.getMessage());

        return problema;
    }
}