package de.msg.javatraining.donationmanager.config.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice
public class RestResponseStatusExceptionResolver {

    @ExceptionHandler(value = {SQLIntegrityConstraintViolationException.class})
    private ResponseEntity<String> handleUniqueConstraintViolation(SQLIntegrityConstraintViolationException ex){
        return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
    }

}
