package com.finance.auth_service.infrastructure.security.service;


import lombok.RequiredArgsConstructor;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

import com.finance.auth_service.infrastructure.persistance.entity.UserEntity;
import com.finance.auth_service.infrastructure.persistance.repository.UserRepository;

/**
 * @Service
 * O QUE FAZ: Registra esta classe como um Bean de serviço no Spring.
 * TÉCNICO: O Spring Security vai procurar automaticamente por alguém que implemente
 * a interface 'UserDetailsService' para usar como estratégia de autenticação.
 * Ao colocar @Service aqui, o Spring encontra essa classe e a conecta na segurança.
 */
@Service
@RequiredArgsConstructor // Cria um construtor com os campos 'final' (Injeção de dependência limpa)
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * loadUserByUsername
     * O QUE FAZ: O método obrigatório da interface. É chamado toda vez que alguém tenta logar.
     * * @param email O "username" enviado na requisição de login (no nosso caso, o email).
     * @return UserDetails: Um objeto padrão do Spring Security com os dados do usuário.
     * @throws UsernameNotFoundException Se o email não existir no banco.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        
        // 1. Busca no Banco de Dados usando nosso Repository
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o email: " + email));

        // 2. Converte (Mapeia) nossa Entidade para o objeto User do Spring Security
        // O Spring Security precisa de 3 coisas: Username, Password e Autoridades (Roles)
        return new User(
                userEntity.getEmail(),
                userEntity.getPassword(), // O Spring vai comparar essa senha (hash) com a que o usuário digitou
                Collections.singletonList(new SimpleGrantedAuthority(userEntity.getRole().name()))
        );
    }
}