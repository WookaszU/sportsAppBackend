package pl.edu.agh.sportsApp.exceptionHandler.exceptions;

public class ValidationException extends RuntimeException {
    public ValidationException(String ex) {
        super(ex);
    }
}
