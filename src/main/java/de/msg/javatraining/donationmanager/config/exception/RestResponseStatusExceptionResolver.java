package de.msg.javatraining.donationmanager.config.exception;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.PropertyValueException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.sql.SQLIntegrityConstraintViolationException;

@Slf4j
@ControllerAdvice
public class RestResponseStatusExceptionResolver {

    @ExceptionHandler(value = {SQLIntegrityConstraintViolationException.class})
    private ResponseEntity<String> handleUniqueConstraintViolation(SQLIntegrityConstraintViolationException ex) {
        String fullExMessage = ex.getMessage();
        String message = fullExMessage.substring(fullExMessage.indexOf('['), fullExMessage.indexOf(']') + 1);
        log.error(ex.getMessage());
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {DataIntegrityViolationException.class})
    private ResponseEntity<String> handleDublicatedKeyException(DataIntegrityViolationException ex) {
        String fullExMessage = ex.getMessage();
        String message = fullExMessage.substring(fullExMessage.indexOf('[') + 1, fullExMessage.indexOf(']'));
        log.error(ex.getMessage());
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    private ResponseEntity<String> handleSqlExceptionHandler(Exception ex) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {InvalidRefreshTokenException.class})
    private ResponseEntity<String> handleInvalidRefreshToken(InvalidRefreshTokenException exception) {
        log.warn(exception.getMessage());
        return new ResponseEntity<String>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({UsernameNotFoundException.class})
    private ResponseEntity<String> handleUsernameNotFoundException(Exception exception) {
        log.error(exception.getMessage());
        return new ResponseEntity<String>("Bad credentials", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({BadCredentialsException.class})
    private ResponseEntity<String> handleBadPasswordException(Exception exception) {
        log.error(exception.getMessage());
        return new ResponseEntity<String>("Bad credentials", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({PropertyValueException.class})
    private ResponseEntity<String> handlePropertyValueException(Exception exception) {
        log.error(exception.getMessage());
        return new ResponseEntity<String>("Bad request", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({EntityNotFoundException.class})
    private ResponseEntity<String> handlePropertyValueException(EntityNotFoundException exception) {
        log.error(exception.getMessage());
        return new ResponseEntity<String>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({DisabledException.class})
    private ResponseEntity<String> handleUserDisabledException() {
        log.error("Account disabled");
        return new ResponseEntity<String>("Your account is disabled contact us for more info!", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({InvalidRequestException.class})
    private ResponseEntity<String> handleInvalidRequestException(InvalidRequestException exception) {
        log.warn(exception.getMessage());
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({RuntimeException.class})
    private ResponseEntity<String> handleInvalidRequestException(RuntimeException exception) {
        log.error(exception.getMessage());
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
