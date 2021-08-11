package com.mgl.accountsservice.exceptions;

/**
 * A generic DatabaseException for all the errors that could occur within a Database.
 */
public class DatabaseException extends RuntimeException {

    /**
     * Default constructor.
     *
     * @param message The message to display.
     * @param cause The cause of the error.
     */
    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
