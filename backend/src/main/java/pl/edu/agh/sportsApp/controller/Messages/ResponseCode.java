package pl.edu.agh.sportsApp.controller.Messages;

public enum ResponseCode {

    SUCCESS,                // "OK"
    EMAIL_ERROR,            // "Confirmation email could not be send."
    ALREADY_REGISTERED,     // "User with this email already registered."
    TOKEN_NOT_FOUND,        // "Token not found in database."
    TOKEN_EXPIRED
}
