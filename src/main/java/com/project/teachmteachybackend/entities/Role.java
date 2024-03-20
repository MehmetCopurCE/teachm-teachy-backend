package com.project.teachmteachybackend.entities;

public enum Role {
    ROLE_USER ("USER"),
    ROLE_ADMIN ("ADMIN"),
    ;

    private final String value;

    Role(String role) {
        this.value = role;
    }

    public String getValue(){
        return this.value;
    }
}
