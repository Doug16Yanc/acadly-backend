package douglas.events.infraestructure.config.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class KeepAliveScheduler {

    private static final Logger log = LoggerFactory.getLogger(KeepAliveScheduler.class);
    private final RestTemplate restTemplate = new RestTemplate();

    @Scheduled(fixedRate = 100000)
    public void pingSelf() {
        log.info("Rodando o scheduler: enviando ping para manter o serviço ativo...");
        
        try {
            String url = "https://events-backend-c8sl.onrender.com/";
            String response = restTemplate.getForObject(url, String.class);
            log.info("Ping bem-sucedido. Resposta: {}", response);
        } catch (Exception e) {
            log.error("Erro ao fazer o ping: {}", e.getMessage());
        }
    }
}