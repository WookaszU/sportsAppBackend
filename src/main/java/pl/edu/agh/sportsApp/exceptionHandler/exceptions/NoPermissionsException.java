package pl.edu.agh.sportsApp.exceptionHandler.exceptions;

public class NoPermissionsException extends RuntimeException {

    public NoPermissionsException(String ex) {
        super(ex);
    }

}