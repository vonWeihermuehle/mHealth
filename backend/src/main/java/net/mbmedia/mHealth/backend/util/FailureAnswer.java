package net.mbmedia.mHealth.backend.util;

public enum FailureAnswer {

    SOME("Default Error"),
    NO_PERMISSION("No Permission"),
    NO_USER_FOUND("No User found"),
    LOGIN_FAILED("Login data wrong"),
    REGISTER_FAILED("Register failed"),
    ALREADY_USED("Already used");

    private final String message;

    FailureAnswer(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
