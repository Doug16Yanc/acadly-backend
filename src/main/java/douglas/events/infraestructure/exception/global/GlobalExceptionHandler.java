package douglas.events.infraestructure.exception.global;

import douglas.events.infraestructure.exception.local.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DateConflictException.class)
    public ResponseEntity<ErrorResponse> handleDateConflict(DateConflictException ex) {
        ErrorResponse body = new ErrorResponse(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(ListEmptyException.class)
    public ResponseEntity<ErrorResponse> handleEventListEmpty(ListEmptyException ex) {
        ErrorResponse body = new ErrorResponse(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEventNotFound(NotFoundException ex) {
        ErrorResponse body = new ErrorResponse(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(AlreadyActiveEventException.class)
    public ResponseEntity<ErrorResponse> handleEventAlreadyActive(AlreadyActiveEventException ex) {
        ErrorResponse body = new ErrorResponse(ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    @ExceptionHandler(AdminNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEventAdminNotFound(AdminNotFoundException ex) {
        ErrorResponse body = new ErrorResponse(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEventEmployeeNotFound(EmployeeNotFoundException ex) {
        ErrorResponse body = new ErrorResponse(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(EnrollmentException.class)
    public ResponseEntity<ErrorResponse> handleEventEnrollment(EnrollmentException ex) {
        ErrorResponse body = new ErrorResponse(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(EventNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEventEventNotFound(EventNotFoundException ex) {
        ErrorResponse body = new ErrorResponse(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(ParticipantNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEventParticipantNotFound(ParticipantNotFoundException ex) {
        ErrorResponse body = new ErrorResponse(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEventUsernameAlreadyExists(UsernameAlreadyExistsException ex) {
        ErrorResponse body = new ErrorResponse(ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ErrorResponse> handleInvalidToken(InvalidTokenException ex) {
        ErrorResponse body = new ErrorResponse(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(ValidationCodeException.class)
    public ResponseEntity<ErrorResponse> handleValidationCode(ValidationCodeException ex) {
        ErrorResponse body = new ErrorResponse(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(ParticipantAlreadyEnrolledException.class)
    public ResponseEntity<ErrorResponse> handleParticipantAlreadyEnrolled(ParticipantAlreadyEnrolledException ex) {
        ErrorResponse body = new ErrorResponse(ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }
}
