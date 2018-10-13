package pl.edu.agh.sportsApp.exceptionHandler.exceptions;

public class ServerMailException extends RuntimeException {
    public ServerMailException(String ex) {
        super(ex);
    }
}
