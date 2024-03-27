package com.project.teachmteachybackend.exceptions;

public class FriendRequestNotFoundException extends RuntimeException{
    public FriendRequestNotFoundException(String message) {
        super(message);
    }
    public FriendRequestNotFoundException(){
        super("Friend request not found");
    }


}
