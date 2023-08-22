package de.msg.javatraining.donationmanager.config.exception;

public class DonatorNotFoundException extends RuntimeException{
    public DonatorNotFoundException(String message){
        super(message);
    }
}
