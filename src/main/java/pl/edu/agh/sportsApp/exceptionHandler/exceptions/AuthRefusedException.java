package pl.edu.agh.sportsApp.exceptionHandler.exceptions;

public class AuthRefusedException extends RuntimeException {

    public AuthRefusedException(String ex) {
        super(ex);
    }

}
