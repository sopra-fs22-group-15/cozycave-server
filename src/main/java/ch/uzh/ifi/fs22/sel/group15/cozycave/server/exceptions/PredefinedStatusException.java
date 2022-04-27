package ch.uzh.ifi.fs22.sel.group15.cozycave.server.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public enum PredefinedStatusException {

    EMAIL_INVALID(HttpStatus.BAD_REQUEST, "email is not valid"),
    PASSWORD_TOO_SHORT(HttpStatus.BAD_REQUEST, "password is too short");

    private final HttpStatus status;
    private final String message;

    PredefinedStatusException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public ResponseStatusException getException() {
        return new ResponseStatusException(status, message);
    }

    public void throwException() {
        throw getException();
    }
}
