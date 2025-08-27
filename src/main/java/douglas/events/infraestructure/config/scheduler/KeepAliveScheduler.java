package douglas.events.infraestructure.config.scheduler;

import douglas.events.infraestructure.repository.PersonRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@AllArgsConstructor
@Component
public class KeepAliveScheduler {

    private static final Logger log = LoggerFactory.getLogger(KeepAliveScheduler.class);
    private final RestTemplate restTemplate = new RestTemplate();
    private PersonRepository personRepository;


    @Scheduled(fixedRate = 100000, initialDelay = 30000)
    public void pingApiRender() {
        log.info("Rodando o scheduler: enviando ping para manter o serviço ativo...");
        
        try {
            String url = "https://events-backend-c8sl.onrender.com/";
            String response = restTemplate.getForObject(url, String.class);
            log.info("Ping bem-sucedido. Resposta: {}", response);
        } catch (Exception e) {
            log.error("Erro ao fazer o ping: {}", e.getMessage());
        }
    }

    @Scheduled(cron = "0 0 20 * * ?")
    public void pingDatabaseSupabase() {
        log.info("Executando query para manter o banco do Supabase ativo...");
        try {
            long count = personRepository.count();
            log.info("Query para o Supabase bem-sucedida. Contagem de pessoas: {}", count);
        } catch (Exception e) {
            log.error("Erro ao executar query para o Supabase: {}", e.getMessage());
        }
    }
}