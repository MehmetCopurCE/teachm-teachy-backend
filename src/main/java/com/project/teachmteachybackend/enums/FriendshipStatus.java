package com.project.teachmteachybackend.enums;

public enum FriendshipStatus {
    PENDING("Friendship request is waiting for approval."),
    ACCEPTED("Friendship is established."),
    REJECTED("Friendship request is rejected."),
    ;

    private final String value;

    FriendshipStatus(String value) {
        this.value = value;
    }
    public String getValue(){
        return this.value;
    }
}
