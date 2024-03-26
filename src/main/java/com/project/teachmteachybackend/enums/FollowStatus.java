package com.project.teachmteachybackend.enums;

public enum FollowStatus {
    PENDING("Friendship request is waiting for approval."),
    ACCEPTED("Friendship is established."),
    REJECTED("Friendship request is rejected."),
    ;

    private final String value;

    FollowStatus(String value) {
        this.value = value;
    }
    public String getValue(){
        return this.value;
    }
}
