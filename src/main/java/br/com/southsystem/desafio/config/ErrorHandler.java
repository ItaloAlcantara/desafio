package br.com.southsystem.desafio.config;

import br.com.southsystem.desafio.exception.AssociadoException;
import br.com.southsystem.desafio.exception.PautaException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandler {

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AssociadoException.class)
    public String handleAssociadoException(AssociadoException e){
        return e.getMessage();
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(PautaException.class)
    public String handlePautaException (PautaException e){
        return e.getMessage();
    }
}
