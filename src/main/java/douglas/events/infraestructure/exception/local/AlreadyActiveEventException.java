package douglas.events.infraestructure.exception.local;

public class AlreadyActiveEventException extends RuntimeException {
    public AlreadyActiveEventException(String message) {
        super(message);
    }
}
