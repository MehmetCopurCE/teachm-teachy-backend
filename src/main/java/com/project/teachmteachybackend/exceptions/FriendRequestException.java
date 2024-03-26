package com.project.teachmteachybackend.exceptions;

public class FriendRequestException extends RuntimeException{

    public FriendRequestException(String message) {
        super(message);
    }

    public FriendRequestException(){
        super("Friend is already followed!");
    }
}
