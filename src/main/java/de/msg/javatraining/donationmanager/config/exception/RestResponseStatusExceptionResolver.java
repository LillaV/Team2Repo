package de.msg.javatraining.donationmanager.config.exception;

import org.hibernate.PropertyValueException;
import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice
public class RestResponseStatusExceptionResolver {

    @ExceptionHandler(value = {SQLIntegrityConstraintViolationException.class})
    private ResponseEntity<String> handleUniqueConstraintViolation(SQLIntegrityConstraintViolationException ex){
        return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    private ResponseEntity<String> handleSqlExceptionHandler(Exception ex){
        return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {InvalidRefreshTokenException.class})
    private  ResponseEntity<String> handleInvalidRefreshToken(InvalidRefreshTokenException exception){
        return new ResponseEntity<String>(exception.getMessage(),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({UsernameNotFoundException.class})
    private ResponseEntity<String> handleUsernameNotFoundException(Exception exception){
        return new ResponseEntity<String>("bad username",HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({BadCredentialsException.class})
    private ResponseEntity<String> handleBadPasswordException(Exception exception){
        return new ResponseEntity<String>("bad password",HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({PropertyValueException.class})
    private ResponseEntity<String> handlePropertyValueException(Exception exception){
        return new ResponseEntity<String>("bad request",HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({InvalidDonationException.class})
    private ResponseEntity<String> handleInvalidDonationException(Exception exception){
        return new ResponseEntity<String>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({DonatorNotFoundException.class})
    private ResponseEntity<String> handleBadPasswordException(DonatorNotFoundException exception){
        return new ResponseEntity<String>(exception.getMessage(),HttpStatus.BAD_REQUEST);
    }
}
