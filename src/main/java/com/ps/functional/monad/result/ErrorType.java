package com.ps.functional.monad.result;

import java.util.Objects;

public class ErrorType {

    public enum Severity {
        CRITICAL, MODERATE, MINOR, WARNING, DEBUG, UNKNOWN
    }

    public enum ErrorSource {
        INTERNAL, CLIENT, EXTERNAL, UNKNOWN, MULTIPLE
    }

    private final String message;

    private final int code;

    private final Severity severity;

    private final ErrorSource errorSource;

    private final String errorName;

    private ErrorType(
            String message,
            int code,
            ErrorType.Severity severity,
            ErrorType.ErrorSource errorSource,
            String name) {
        this.message = Objects.requireNonNull(message, "message");
        this.code = code;
        this.severity = Objects.requireNonNull(severity, "severity");
        this.errorSource = Objects.requireNonNull(errorSource, "errorSource");
        this.errorName = Objects.requireNonNull(name, "errorName");
    }

    public static ErrorType of(
            String message,
            int code,
            ErrorType.Severity severity,
            ErrorType.ErrorSource errorSource,
            String errorName) {
        return new ErrorType(message, code, severity, errorSource, errorName);
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }

    public Severity getSeverity() {
        return severity;
    }

    public ErrorSource getErrorSource() {
        return errorSource;
    }

    public String getErrorName() {
        return errorName;
    }

    public String show() {
        return code + " - " + errorName + ", Sev: " + severity.toString() +
                ", Source: " + errorSource.toString() + ", Message: " + message;
    }
    
}
