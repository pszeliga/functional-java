package com.ps.functional.monad.result;

import java.util.NoSuchElementException;

public final class Failure<T> implements Result<T> {

    private static final String NO_MORE_INFO = "";

    private final ErrorType error;

    private final String optionalErrorMessage;

    private Failure(ErrorType error) {
        this.error = error;
        this.optionalErrorMessage = NO_MORE_INFO;
    }

    private Failure(ErrorType error, String optionalErrorMessage) {
        this.error = error;
        this.optionalErrorMessage = optionalErrorMessage;
    }

    public static <T> Result<T> of(ErrorType error) {
        return new Failure<>(error);
    }

    public static <T> Result<T> of(ErrorType error, String errorMessage) {
        return new Failure<>(error, errorMessage);
    }

    @Override
    public boolean isSuccess() {
        return false;
    }

    @Override
    public ErrorType getError() {
        return error;
    }

    @Override
    public String getOptionalErrorMessage() {
        return optionalErrorMessage;
    }

    @Override
    public T getResult() {
        throw new NoSuchElementException("There is no result is Failure");
    }

    @Override
    public String toString() {
        return String.format("Failure[%s, optional: %s]", error.show(), optionalErrorMessage);
    }

}
