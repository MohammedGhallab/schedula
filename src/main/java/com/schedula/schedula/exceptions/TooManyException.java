package com.schedula.schedula.exceptions;

public class TooManyException extends Exception {
    public TooManyException() {

    }

    public TooManyException(String error) {
        super(error);
    }
}
