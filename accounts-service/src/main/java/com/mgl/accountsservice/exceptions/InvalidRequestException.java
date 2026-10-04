package com.mgl.accountsservice.exceptions;

/** A typed public API failure with a safe client-facing message. */
public class InvalidRequestException extends IllegalArgumentException {

    /** Constructs a failure with a safe public message. */
    public InvalidRequestException(String message) {
        super(message);
    }
}
