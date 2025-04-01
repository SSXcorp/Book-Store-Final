package mate.academy.bookstoreprod.exception.handler;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.List;
import java.util.stream.Collectors;
import mate.academy.bookstoreprod.exception.EntityAlreadyExistsException;
import mate.academy.bookstoreprod.exception.EntityNotFoundException;
import mate.academy.bookstoreprod.exception.RegistrationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    @ApiResponse(responseCode = "404", description = "Entity Not Found",
                    content = @Content(mediaType = "application/json"))
    public ResponseEntity<String> handleEntityNotFoundExceptions(EntityNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ApiResponse(responseCode = "400", description = "Validation Failed",
            content = @Content(mediaType = "application/json"))
    public ResponseEntity<List<String>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getAllErrors().stream()
                .map(objectError -> {
                    if (objectError instanceof FieldError fieldError) {
                        return "Field '" + fieldError.getField() + "' "
                                + fieldError.getDefaultMessage();
                    } else {
                        return objectError.getDefaultMessage();
                    }
                })
                .collect(Collectors.toList());

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    @ApiResponse(responseCode = "409", description = "Entity Already Exists",
                    content = @Content(mediaType = "application/json"))
    public ResponseEntity<String> handleEntityAlreadyExistsExceptions(
            EntityAlreadyExistsException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    @ApiResponse(responseCode = "500", description = "Internal Server Error",
            content = @Content(mediaType = "application/json"))
    public ResponseEntity<String> handleAllExceptions(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RegistrationException.class)
    public ResponseEntity<String> handleRegistrationException(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(),
                HttpStatus.CONFLICT);
    }
}
