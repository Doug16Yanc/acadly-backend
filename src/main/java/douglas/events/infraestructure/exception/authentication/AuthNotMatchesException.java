package douglas.events.infraestructure.exception.authentication;


public class AuthNotMatchesException extends RuntimeException {
    public AuthNotMatchesException() {
        super("Authentication failed, email and password does not match.");
    }
}