package de.msg.javatraining.donationmanager.config.exception;

public class InvalidRequestException extends RuntimeException{
    public  InvalidRequestException(String message){
        super(message);
    }
}
