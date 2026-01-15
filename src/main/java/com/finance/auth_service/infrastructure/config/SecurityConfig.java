package com.finance.auth_service.infrastructure.config;

// IMPORTS OBRIGATÓRIOS (Sem eles o código fica vermelho)
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.finance.auth_service.infrastructure.security.service.UserDetailsServiceImpl;

/**
 * @Configuration
 * O QUE FAZ: Marca esta classe como uma fonte de definições de beans.
 * TÉCNICO: O Spring container processa essa classe na inicialização para gerar instâncias de objetos.
 */
@Configuration

/**
 * @EnableWebSecurity
 * O QUE FAZ: Aplica a configuração de segurança à aplicação Web global.
 * TÉCNICO: Importa a configuração WebSecurityConfiguration e permite que personalizemos
 * o HttpSecurity. (Você estava usando @EnableSpringDataWebSupport, que é para outra coisa).
 */
@EnableWebSecurity 
public class SecurityConfig {

    /**
     * @Bean
     * O QUE FAZ: Instrui o Spring a gerenciar o objeto retornado por este método.
     * TÉCNICO: O objeto 'SecurityFilterChain' será injetado no contexto do Spring e
     * passará a interceptar todas as requisições que chegam na porta do servidor.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // 1. O OBJETO 'http' (HttpSecurity)
        // É um "Builder". Pense nele como uma prancheta em branco onde desenhamos as regras.
        // Ele permite encadear métodos (.algo().outro()) para configurar o filtro.
        http
            
            // 2. MÉTODO .csrf(...)
            // Cross-Site Request Forgery.
            // PARAMETRO: AbstractHttpConfigurer::disable (Reference Method)
            // EXPLICAÇÃO: Passamos uma função que desliga essa proteção.
            // MOTIVO: Como usamos JWT (que não fica salvo em cookies do navegador),
            // o CSRF não é necessário e atrapalha requisições POST.
            .csrf(AbstractHttpConfigurer::disable)

            // 3. MÉTODO .sessionManagement(...)
            // Configura como o servidor lida com a "memória" de quem está logado.
            .sessionManagement(session -> session
                // PARAMETRO: SessionCreationPolicy.STATELESS
                // TÉCNICO: Define que o Spring Security NÃO criará HttpSession.
                // IMPACTO: O servidor não guarda estado. Toda requisição deve conter o Token.
                // Isso é vital para escalar microserviços (criar várias cópias do serviço sem quebrar o login).
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // 4. MÉTODO .authorizeHttpRequests(...)
            // Inicia o registro de regras de URL (quem pode acessar o quê).
            // Recebe um lambda (authorize -> ...) que define as rotas.
            .authorizeHttpRequests(authorize -> authorize
                
                // 4.1. .requestMatchers(...)
                // "Se a URL for essa..."
                // PARAMETRO: "/auth/**" significa qualquer coisa depois de /auth/ (login, registro).
                .requestMatchers("/auth/**")
                
                // 4.2. .permitAll()
                // "...então DEIXE PASSAR sem verificar nada."
                .permitAll()

                // 4.3. .anyRequest()
                // "Para todas as outras URLs que não listei acima..."
                .anyRequest()
                
                // 4.4. .authenticated()
                // "...EXIJA que o usuário tenha um token válido."
                // Se não tiver, retorna erro 403 Forbidden.
                .authenticated()
            );

        // 5. MÉTODO .build()
        // Finaliza a configuração do Builder 'http' e cria a instância imutável
        // do SecurityFilterChain para o Spring usar.
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){

        return new BCryptPasswordEncoder(); 
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider (UserDetailsServiceImpl userDetailsService){

        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);

        authProvider.setPasswordEncoder(passwordEncoder());
    }
}