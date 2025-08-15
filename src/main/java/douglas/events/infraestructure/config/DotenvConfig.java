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
        System.setProperty("PUBLIC_KEY", Objects.requireNonNull(dotenv.get("PUBLIC_KEY")));
        System.setProperty("PRIVATE_KEY", Objects.requireNonNull(dotenv.get("PRIVATE_KEY")));
        System.setProperty("ISSUER", Objects.requireNonNull(dotenv.get("ISSUER")));
    }
}
