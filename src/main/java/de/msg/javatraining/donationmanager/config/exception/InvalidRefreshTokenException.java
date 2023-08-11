package de.msg.javatraining.donationmanager.config.exception;

public class InvalidRefreshTokenException extends RuntimeException{
    public InvalidRefreshTokenException(String errMessage){
        super(errMessage);
    }
}
