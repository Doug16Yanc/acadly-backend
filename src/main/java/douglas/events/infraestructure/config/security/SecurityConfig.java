package douglas.events.infraestructure.config.security;

import douglas.events.infraestructure.exception.authentication.CustomAccessDeniedHandler;
import douglas.events.infraestructure.exception.authentication.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final SecurityFilter securityFilter;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(a -> a
                        .requestMatchers("/","/auth/login", "/employee/auth", "/auth/register", "/event/get-all-events", "event/get-event-active",
                                "classification/find-by-type/{type}", "/classification/types", "/participant/create-participation/**",
                                "/v3/api-docs/**", "/swagger-ui/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/activity/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/activity/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/event/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/event/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/event/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/employee/register").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/employee/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/employee/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/employee/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/enrollment/**").hasAnyRole("ADMIN", "EMPLOYEE")
                        .requestMatchers(HttpMethod.POST, "/enrollment/**").hasAnyRole("ADMIN", "EMPLOYEE")
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(e -> e
                        .accessDeniedHandler(accessDeniedHandler)
                        .authenticationEntryPoint(authenticationEntryPoint));
        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

