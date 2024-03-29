package com.project.teachmteachybackend.exceptions;


public class UsernameInUseException extends RuntimeException{
    public UsernameInUseException(String message) {
        super(message);
    }

    public UsernameInUseException(){
        super("Username in use!");
    }
}
