package pl.edu.agh.sportsApp.dto;

public enum ResponseCode {

    // register
    EMAIL_ERROR,                    // "Confirmation email could not be send."
    ALREADY_REGISTERED,             // "User with this email already registered."

    // token confirm and resend token
    TOKEN_NOT_FOUND,                // "Token not found in database."
    NOT_REGISTERED,                 // "No email resend, not registered before."
    TOKEN_EXPIRED,                  // "Expired token."

    // all requests
    METHOD_ARGS_NOT_VALID,          // "Method arguments are not valid e.g. empty fields, wrong email.
    NEED_REQUIRED_RIGHTS,             // "Need required user rights to gain access."

    // login
    CONFIRM_YOUR_ACCOUNT,           // "Confirm your new account."
    WRONG_LOGIN_OR_PASSWORD,        // "Wrong login or password."

    // image controller
    EMPTY_FILE,
    INVALID_IMAGE_PROPORTIONS,
    FILE_TOO_BIG,
    WRONG_FORMAT,                   // "Wrong file format."
    MEDIA_SERVICE_NOT_AVAILABLE,    // "Multimedia service is inaccessible now."
    RESOURCE_NOT_FOUND,             // "Resource was not found on server."

    // whole app
    ACCESS_DENIED                   // "Access denied to resource."
}
