package douglas.events.infraestructure.exception.authentication;


public class AuthAdminNotMatchesException extends RuntimeException {
    public AuthAdminNotMatchesException() {
        super("Authentication failed for admin, email and password does not match.");
    }
}