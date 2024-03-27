package com.project.teachmteachybackend.enums;

public enum AccountPrivacy {
    PUBLIC ("Account is public"),
    PRIVATE ("Account is private"),
    ;

    private final String value;

    AccountPrivacy(String value) {
        this.value = value;
    }

    public String getValue(){
        return this.value;
    }
}