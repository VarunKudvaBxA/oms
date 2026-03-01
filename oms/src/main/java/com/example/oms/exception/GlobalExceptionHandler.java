package com.example.oms.exception;
import org.springframework.web.bind.annotation.*;
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidOrderException.class)
    public String handleInvalidOrder(InvalidOrderException ex) {
        return ex.getMessage();
    }
}
