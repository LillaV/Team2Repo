package de.msg.javatraining.donationmanager.config.exception;

public class InvalidDonationException extends RuntimeException{
    public InvalidDonationException(String msg){
        super(msg);
    }
}
