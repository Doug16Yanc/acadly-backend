package douglas.events.infraestructure.config;


import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;
@Configuration
public class DotenvConfig {

    @Value("${PUBLIC_KEY}")
    private String publicKey;

    @Value("${PRIVATE_KEY}")
    private String privateKey;

    @Value("${ISSUER}")
    private String issuer;

    @PostConstruct
    public void init() {
        System.setProperty("PUBLIC_KEY", publicKey);
        System.setProperty("PRIVATE_KEY", privateKey);
        System.setProperty("ISSUER", issuer);
    }
}
