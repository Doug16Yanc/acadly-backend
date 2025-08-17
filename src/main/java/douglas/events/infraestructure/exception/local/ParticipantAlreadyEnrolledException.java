package douglas.events.infraestructure.exception.local;

public class ParticipantAlreadyEnrolledException extends RuntimeException{
    public ParticipantAlreadyEnrolledException(String message) {
        super(message);
    }
}
