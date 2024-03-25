package com.project.teachmteachybackend.enums;

public enum Role {
    USER ("USER"),
    ADMIN ("ADMIN"),
    ;

    private final String value;

    Role(String role) {
        this.value = role;
    }

    public String getValue(){
        return this.value;
    }
}
