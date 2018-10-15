package pl.edu.agh.sportsApp.dto;

public enum ResponseCode {

    EMAIL_ERROR,                // "Confirmation email could not be send."
    ALREADY_REGISTERED,         // "User with this email already registered."
    TOKEN_NOT_FOUND,            // "Token not found in database."
    NOT_REGISTERED,             // "No email resend, not registered before."
    TOKEN_EXPIRED,              // "Expired token."
    METHOD_ARGS_NOT_VALID,      // "Method arguments are not valid e.g. empty fields, wrong email.

    CONFIRM_YOUR_ACCOUNT,       // "Confirm your new account."
    USER_NOT_EXISTS,            // "User not exists."
    WRONG_LOGIN_OR_PASSWORD,    // "Wrong login or password."

    EMPTY_FILE,
    INVALID_IMAGE_PROPORTIONS,
    FILE_TOO_BIG,
    MEDIA_SERVICE_INACCESIBLE,  // "Multimedia service is inaccessible now."

    ACCESS_DENIED,              // "Access denied to resource."
    RESOURCE_NOT_FOUND          // "Resource was not found on server."
}
