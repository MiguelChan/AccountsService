package com.mgl.accountsservice.integration.clients;

import java.io.IOException;

/** A non-successful HTTP response, distinct from network failures. */
public class HttpStatusException extends IOException {

    private final int statusCode;

    /** Preserves the HTTP status and server error envelope. */
    public HttpStatusException(int statusCode, String body) {
        super("HTTP " + statusCode + ": " + body);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
