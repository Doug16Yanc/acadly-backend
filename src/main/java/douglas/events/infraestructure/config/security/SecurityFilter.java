package douglas.events.infraestructure.config.security;

import douglas.events.infraestructure.model.Person;
import douglas.events.infraestructure.repository.PersonRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final PersonRepository personRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        System.out.println("\n====== INICIANDO SECURITY FILTER (NOVA VERSÃO) ======");
        System.out.println("URL da Requisição: " + request.getRequestURI());

        String token = recoveryToken(request);
        // PRINT 1: Verificar se o token foi recuperado
        System.out.println("[DEBUG] Token recuperado do header: " + token);

        if (token != null && !token.isEmpty()) {
            String username = tokenService.validateToken(token); // username é o email
            // PRINT 2: Verificar o username (email) extraído do token
            System.out.println("[DEBUG] Username validado do token: " + username);

            if (username != null) {
                // PRINT 3: Tentando buscar a pessoa no banco
                System.out.println("[DEBUG] Buscando pessoa no repositório com o email: " + username);
                Person person = personRepository.findByEmail(username);

                if (person != null) {
                    // PRINT 4: Pessoa encontrada, verificar a role
                    System.out.println("[DEBUG] Pessoa encontrada: " + person.getName());
                    if (person.getRole() != null) {
                        System.out.println("[DEBUG] Role da pessoa é: " + person.getRole().name());

                        // Criando a lista de authorities
                        List<SimpleGrantedAuthority> authorities = List.of(
                                new SimpleGrantedAuthority("ROLE_" + person.getRole().name())
                        );

                        // PRINT 5: Verificar a authority criada
                        System.out.println("[DEBUG] Authority criada: " + authorities.get(0).getAuthority());

                        var authentication = new UsernamePasswordAuthenticationToken(
                                person, null, authorities
                        );

                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);

                        // PRINT 6: Sucesso!
                        System.out.println("[DEBUG] SUCESSO! Usuário autenticado e inserido no SecurityContext.");

                    } else {
                        // PRINT 7: Erro - A pessoa existe mas não tem role
                        System.err.println("[DEBUG] ERRO: Pessoa encontrada, mas a 'role' dela é NULA no banco de dados.");
                    }
                } else {
                    // PRINT 8: Erro - Pessoa não encontrada no banco
                    System.err.println("[DEBUG] ERRO: Nenhuma pessoa encontrada no banco de dados com o email: " + username);
                }
            }
        } else {
            System.out.println("[DEBUG] Nenhum token válido encontrado na requisição.");
        }

        System.out.println("====== FINALIZANDO SECURITY FILTER ======\n");
        filterChain.doFilter(request, response);
    }

    private String recoveryToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        return authHeader.substring(7);
    }
}