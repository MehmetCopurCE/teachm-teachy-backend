package com.project.teachmteachybackend.exceptions;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ExistEmailException extends RuntimeException {
    public ExistEmailException(String message) {
        super(message);
    }

    public ExistEmailException(){super("Email in use!");
    }
}
