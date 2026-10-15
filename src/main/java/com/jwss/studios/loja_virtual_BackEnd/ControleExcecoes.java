package com.jwss.studios.loja_virtual_BackEnd;

import com.jwss.studios.loja_virtual_BackEnd.model.dto.ObjetoErroDTO;
import org.hibernate.exception.ConstraintViolationException;
import org.jspecify.annotations.Nullable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.SQLException;
import java.util.List;

@RestControllerAdvice
@ControllerAdvice
public class ControleExcecoes extends ResponseEntityExceptionHandler {

    // 1. CAPTURA ERROS GERAIS DO SISTEMA (Exception, RuntimeException, Throwable)
    // REMOVIDO o @Override daqui, pois criamos um handler dedicado para exceções genéricas globais
    @ExceptionHandler({Exception.class, RuntimeException.class, Throwable.class})
    public ResponseEntity<Object> handleExceptionGeral(Exception ex, WebRequest request) {
        ObjetoErroDTO objetoErroDTO = new ObjetoErroDTO();
        objetoErroDTO.setErro(ex.getMessage());
        objetoErroDTO.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value() + " ==> " + HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());

        return new ResponseEntity<>(objetoErroDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // 2. SOBRESCRITA DO MÉTODO INTERNO DO SPRING (Gerencia dados de validação do JSON e corpo ausente)
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {

        ObjetoErroDTO objetoErroDTO = new ObjetoErroDTO();
        StringBuilder msg = new StringBuilder();

        if (ex instanceof MethodArgumentNotValidException) {
            List<ObjectError> list = ((MethodArgumentNotValidException) ex).getBindingResult().getAllErrors();
            for (ObjectError objectError : list) {
                msg.append(objectError.getDefaultMessage());
            }
        } else if (ex instanceof HttpMessageNotReadableException) {
            msg.append("Não esta sendo enviado dados para corpo da requisição");
        } else {
            msg.append(ex.getMessage());
        }

        objetoErroDTO.setErro(msg.toString());
        HttpStatus statusEnum = HttpStatus.valueOf(statusCode.value());
        objetoErroDTO.setCode(statusCode.value() + " ==> " + statusEnum.getReasonPhrase());

        return new ResponseEntity<>(objetoErroDTO, statusCode);
    }

    // 3. EXCEÇÃO CUSTOMIZADA MENTORIA JAVA
    @ExceptionHandler(ExceptionMentoriaJava.class)
    protected ResponseEntity<Object> handleExceptionCustom(ExceptionMentoriaJava ex) {
        ObjetoErroDTO objetoErroDTO = new ObjetoErroDTO();
        objetoErroDTO.setErro(ex.getMessage());
        objetoErroDTO.setCode(String.valueOf(HttpStatus.OK));

        return new ResponseEntity<>(objetoErroDTO, HttpStatus.OK);
    }

    // 4. CAPTURA ERROS NA PARTE DO BANCO DE DADOS
    @ExceptionHandler({DataIntegrityViolationException.class, ConstraintViolationException.class, SQLException.class})
    protected ResponseEntity<Object> handleExceptionDataIntegry(Exception ex){
        ObjetoErroDTO objetoErroDTO = new ObjetoErroDTO();
        String msg = "";

        if (ex instanceof DataIntegrityViolationException){
            msg = "Erro de integridade do banco: " + (ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage());
        } else if (ex instanceof ConstraintViolationException) {
            msg = "Erro de chave estrngeira: " + (ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage());
        } else if (ex instanceof SQLException) {
            msg = "Erro de SQL do banco: " +
                    (((SQLException) ex).getCause() != null && ((SQLException) ex).getCause().getCause() != null
                            ? ((SQLException) ex).getCause().getCause().getMessage() : ex.getMessage());
        } else {
            msg = ex.getMessage();
        }

        objetoErroDTO.setErro(msg);
        objetoErroDTO.setCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
        return new ResponseEntity<>(objetoErroDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
