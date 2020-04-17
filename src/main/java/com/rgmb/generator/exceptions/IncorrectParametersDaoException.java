package com.rgmb.generator.exceptions;

public class IncorrectParametersDaoException extends Exception {
    public IncorrectParametersDaoException() {
        super();
    }

    public IncorrectParametersDaoException(String message) {
        super(message);
    }

    public IncorrectParametersDaoException(String message, Throwable cause) {
        super(message, cause);
    }

    public IncorrectParametersDaoException(Throwable cause) {
        super(cause);
    }

    protected IncorrectParametersDaoException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
