package com.scarfox.jack.exception;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ProblemDetail tratarRecursoNaoEncontrado(RecursoNaoEncontradoException exception) {
        ProblemDetail problema = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);

        problema.setTitle("Recurso não encontrado");
        problema.setDetail(exception.getMessage());

        return problema;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail tratarArgumentoInvalido(IllegalArgumentException exception) {
        exception.printStackTrace();

        ProblemDetail problema = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problema.setTitle("Requisição inválida");
        problema.setDetail(exception.getMessage() != null ? exception.getMessage() : "Erro de argumento sem mensagem.");

        return problema;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> tratarErroValidacao(MethodArgumentNotValidException exception) {
        ProblemDetail problema = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

        String detalhes = exception.getBindingResult().getFieldErrors().stream()
            .map(erro -> erro.getField() + ": " + erro.getDefaultMessage())
            .collect(Collectors.joining("; "));

        problema.setTitle("Erro de validação");
        problema.setDetail(detalhes);

        return ResponseEntity.badRequest().body(problema);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ProblemDetail> tratarErroHttp(ResponseStatusException exception) {
        ProblemDetail problema = ProblemDetail.forStatus(exception.getStatusCode());

        problema.setTitle("Erro ao processar requisição");
        problema.setDetail(exception.getReason());

        return ResponseEntity.status(exception.getStatusCode()).body(problema);
    }

    @ExceptionHandler(ConflitoEstadoOcorrenciaException.class)
    public ProblemDetail tratarConflitoEstado(ConflitoEstadoOcorrenciaException exception) {
        ProblemDetail problema = ProblemDetail.forStatus(HttpStatus.CONFLICT);

        problema.setTitle("Conflito no estado da ocorrência");
        problema.setDetail(exception.getMessage());

        return problema;
    }
}