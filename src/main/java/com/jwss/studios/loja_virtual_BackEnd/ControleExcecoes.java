package com.jwss.studios.loja_virtual_BackEnd;

import com.jwss.studios.loja_virtual_BackEnd.model.dto.ObjetoErroDTO;
import org.hibernate.exception.ConstraintViolationException;
import org.jspecify.annotations.Nullable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.SQLException;
import java.util.List;
// Capitura Exceções do projeto
@RestControllerAdvice
@ControllerAdvice
public class ControleExcecoes extends ResponseEntityExceptionHandler {
    @ExceptionHandler({Exception.class,RuntimeException.class, Throwable.class})
    @Override
    protected @Nullable ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {

        ObjetoErroDTO objetoErroDTO = new ObjetoErroDTO();
        String msg = "";

        if (ex instanceof MethodArgumentNotValidException){

            List<ObjectError> list = ((MethodArgumentNotValidException) ex).getBindingResult().getAllErrors();

            for (ObjectError objectError : list){
                msg += objectError.getDefaultMessage() + "\n";

            }
        }else {
            msg = ex.getMessage();
        }
        objetoErroDTO.setErro(msg);
        // converte para antigo HttpStatus
        HttpStatus statusEnum = HttpStatus.valueOf(statusCode.value());
        objetoErroDTO.setCode(statusCode.value()+ "==> "+ statusEnum.getReasonPhrase());
        return new ResponseEntity<Object>(objetoErroDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    // Capitura erro na parte do banco
    @ExceptionHandler({DataIntegrityViolationException.class, ConstraintViolationException.class, SQLException.class})
    protected ResponseEntity<Object> handleExceptionDataIntegry(Exception ex){
        ObjetoErroDTO objetoErroDTO = new ObjetoErroDTO();
        String msg = "";
        if (ex instanceof DataIntegrityViolationException){
            msg = "Erro de integridade do banco: "+((DataIntegrityViolationException)ex).getCause().getCause().getMessage();
        }
        if (ex instanceof ConstraintViolationException){
            msg =  "Erro de chave estrngeira: " + ((ConstraintViolationException)ex).getCause().getCause().getMessage();
        }
        if (ex instanceof SQLException){
            msg = "Erro de SQL do banco: "+((SQLException)ex).getCause().getCause().getMessage();
        }else {
            msg = ex.getMessage();
        }
        objetoErroDTO.setErro(msg);
        objetoErroDTO.setCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
        return new ResponseEntity<Object>(objetoErroDTO, HttpStatus.INTERNAL_SERVER_ERROR);

    }



}
