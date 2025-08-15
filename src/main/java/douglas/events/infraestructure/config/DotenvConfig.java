package douglas.events.infraestructure.config;


import jakarta.annotation.PostConstruct;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class DotenvConfig {

    @PostConstruct
    public void init() {
        Dotenv dotenv = Dotenv.configure().load();
        System.setProperty("POSTGRES_HOST", Objects.requireNonNull(dotenv.get("POSTGRES_HOST")));
        System.setProperty("POSTGRES_DB", Objects.requireNonNull(dotenv.get("POSTGRES_DB")));
        System.setProperty("POSTGRES_PORT", Objects.requireNonNull(dotenv.get("POSTGRES_PORT")));
        System.setProperty("POSTGRES_USER", Objects.requireNonNull(dotenv.get("POSTGRES_USER")));
        System.setProperty("POSTGRES_PASSWORD", Objects.requireNonNull(dotenv.get("POSTGRES_PASSWORD")));

        System.setProperty("EMAIL_SENDER", Objects.requireNonNull(dotenv.get("EMAIL_SENDER")));
        System.setProperty("EMAIL_PASSWORD", Objects.requireNonNull(dotenv.get("EMAIL_PASSWORD")));

        System.setProperty("PUBLIC_KEY", Objects.requireNonNull(dotenv.get("PUBLIC_KEY")));
        System.setProperty("PRIVATE_KEY", Objects.requireNonNull(dotenv.get("PRIVATE_KEY")));
        System.setProperty("ISSUER", Objects.requireNonNull(dotenv.get("ISSUER")));
    }
}
