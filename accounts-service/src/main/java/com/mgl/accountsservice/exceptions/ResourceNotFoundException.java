package com.mgl.accountsservice.exceptions;

/** A typed public API failure with a safe client-facing message. */
public class ResourceNotFoundException extends RuntimeException {

    /** Constructs a failure with a safe public message. */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
