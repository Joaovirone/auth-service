package com.finance.auth_service.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

/** 
@Configuration
Indica ao Spring que está é uma classe de confguração
O Spring vai ler essa classe assim que a aplicação subir para definir
os Beans e as regras do sistema
*/
/** 
@EnableWebSecurity
Habilita o suporte à segurança web customizada.
Sem isso, o spring security usa a configuração padrão (bloqueia tudo e gera senha no console)
aqui estamos dizendo: Spring deixa que eu defino as minhas regras
*/
@Configuration
@EnableSpringDataWebSupport
public class SecurityConfig {

/**
@Bean 
Esta anotação diz: "O retorno deste método deve ser gerenciado pelo Spring".
O objeto retornado (SecurityFilterChain) passa a fazer parte do contexto da aplicação
e será usado automaticamente para interceptar todas as requisições HTTP.
     
SecurityFilterChain:
É a corrente de filtros de segurança. Cada requisição que chega na API
passa por essa corrente. Se falhar em algum ponto (ex: sem token), é rejeitada.
     
*/

    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception {

        // 'http' é o objeto construtor (Builder) que define as regras
        http.csrf(AbstractHttpConfigurer::disable)
        /** 
 .csrf(AbstractHttpConfigurer::disable)

O QUE É: Desabilita a proteção contra CSRF (Cross-Site Request Forgery).
POR QUE DESABILITAR? O CSRF é um ataque que depende de cookies de sessão
mantidos no navegador. Como nossa API usará JWT (Tokens) e não cookies/sessão,
essa vulnerabilidade não existe na nossa arquitetura.
Manter habilitado quebraria as chamadas POST/PUT.
NOTA: A sintaxe 'AbstractHttpConfigurer::disable' é uma referência de método (Method Reference),
o padrão novo do Spring Security 6.
*/
        .sessionManagement(session -> session

        )
    }
}
