package pl.edu.agh.sportsApp.controller.dto;

public enum ResponseCode {

    SUCCESS,                // "OK"
    EMAIL_ERROR,            // "Confirmation email could not be send."
    ALREADY_REGISTERED,     // "User with this email already registered."
    WRONG_EMAIL,            // "Typed email is not an email."
    TOKEN_NOT_FOUND,        // "Token not found in database."
    NOT_REGISTERED,         // "No email resend, not registered before."
    TOKEN_EXPIRED,          // "Expired token."
    EMPTY_FIELDS,           // "Fill all required fields in request form."

    CONFIRM_YOUR_ACCOUNT,   // "Confirm your new account."
    ACCOUNT_NOT_EXISTS,      // "Account not exists."
    WRONG_LOGIN_OR_PASSWORD,  // "Wrong login or password."

    EMPTY_FILE,
    INVALID_IMAGE_PROPORTIONS,
    FILE_TOO_BIG,
    MEDIA_SERVICE_INACCESIBLE   // "Multimedia service is inaccessible now."

}
