package pl.edu.agh.sportsApp.exceptionHandler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.edu.agh.sportsApp.dto.ResponseCode;
import pl.edu.agh.sportsApp.exceptionHandler.exceptions.*;

import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;

@ControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        ErrorDTO errorDTO = new ErrorDTO(ResponseCode.METHOD_ARGS_NOT_VALID.name());
        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({PersistenceException.class})
    public ResponseEntity<Object> handleAlreadyExists(PersistenceException ex) {
        ErrorDTO errorDTO = new ErrorDTO(ex.getCause().getMessage());
        return new ResponseEntity<>(errorDTO, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex) {
        ErrorDTO errorDTO = new ErrorDTO(ex.getLocalizedMessage());
        return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<Object> handleIllegalArgument(IllegalArgumentException ex) {
        ErrorDTO errorDTO = new ErrorDTO(ex.getLocalizedMessage());
        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<Object> handleAuthenticationArgument(AccessDeniedException ex) {
        ErrorDTO errorDTO = new ErrorDTO(ex.getMessage());
        return new ResponseEntity<>(errorDTO, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({ServerMailException.class})
    public ResponseEntity<Object> handleServerMailException(ServerMailException ex) {
        ErrorDTO errorDTO = new ErrorDTO(ex.getMessage());
        return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({ConfirmTokenException.class})
    public ResponseEntity<Object> handleConfirmTokenException(ConfirmTokenException ex) {
        ErrorDTO errorDTO = new ErrorDTO(ex.getMessage());
        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({UserAccountException.class})
    public ResponseEntity<Object> handleUserAccountException(UserAccountException ex) {
        ErrorDTO errorDTO = new ErrorDTO(ex.getMessage());
        return new ResponseEntity<>(errorDTO, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({PhotoProcessingException.class})
    public ResponseEntity<Object> handleInvalidPhotoProportionsException(PhotoProcessingException ex) {
        ErrorDTO errorDTO = new ErrorDTO(ex.getMessage());
        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({RegisterException.class})
    public ResponseEntity<Object> handleRegisterException(RegisterException ex) {
        ErrorDTO errorDTO = new ErrorDTO(ex.getMessage());
        return new ResponseEntity<>(errorDTO, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({AuthRefusedException.class})
    public ResponseEntity<Object> handleAuthRefusedException(AuthRefusedException ex) {
        ErrorDTO errorDTO = new ErrorDTO(ex.getMessage());
        return new ResponseEntity<>(errorDTO, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({MediaInaccesibleException.class})
    public ResponseEntity<Object> handleAuthRefusedException(MediaInaccesibleException ex) {
        ErrorDTO errorDTO = new ErrorDTO(ex.getMessage());
        return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}