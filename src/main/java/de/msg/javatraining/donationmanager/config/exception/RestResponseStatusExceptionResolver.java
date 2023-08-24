package de.msg.javatraining.donationmanager.config.exception;

import jakarta.persistence.EntityNotFoundException;
import org.hibernate.PropertyValueException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
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
        return new ResponseEntity<String>("Bad credentials",HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({BadCredentialsException.class})
    private ResponseEntity<String> handleBadPasswordException(Exception exception){
        return new ResponseEntity<String>("Bad credentials",HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({RoleNotFoundException.class})
    private ResponseEntity<String> handleBadPasswordException(RoleNotFoundException exception){
        return new ResponseEntity<String>(exception.getMessage(),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({UserNotFoundException.class})
    private ResponseEntity<String> handleBadPasswordException(UserNotFoundException exception){
        return new ResponseEntity<String>(exception.getMessage(),HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler({PropertyValueException.class})
    private ResponseEntity<String> handlePropertyValueException(Exception exception){
        return new ResponseEntity<String>("Bad request",HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({EntityNotFoundException.class})
    private ResponseEntity<String> handlePropertyValueException(EntityNotFoundException exception){
        return new ResponseEntity<String>(exception.getMessage(),HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler({InvalidDonationException.class})
    private ResponseEntity<String> handleInvalidDonationException(Exception exception){
        return new ResponseEntity<String>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler({DonatorNotFoundException.class})
    private ResponseEntity<String> handleBadPasswordException(DonatorNotFoundException exception){
        return new ResponseEntity<String>(exception.getMessage(),HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler({DisabledException.class})
    private  ResponseEntity<String> handleUserDisabledException(){
        return new ResponseEntity<String>("Your account is disabled contact us for more info!!",HttpStatus.BAD_REQUEST);
    }
}
