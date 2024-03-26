package com.project.teachmteachybackend.exceptions;

public class FriendRequestExistsException extends RuntimeException{

    public FriendRequestExistsException(String message) {
        super(message);
    }

    public FriendRequestExistsException(){
        super("There is already a friend request exist!");
    }
}
