package com.finance.auth_service.infrastructure.persistance.entity;

import com.finance.auth_service.domain.model.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tb_users") // 'user' é palavra reservada no Postgres, evite erros usando um prefixo.
@Data // Lombok: Gera Getters, Setters, toString, etc.
@NoArgsConstructor // Lombok: Construtor vazio (obrigatório pro JPA)
@AllArgsConstructor // Lombok: Construtor com tudo
@Builder // Lombok: Padrão de projeto para criar objetos de forma fluida
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // UUID é mais seguro para microserviços que ID sequencial
    private UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING) // Salva "ROLE_ADMIN" no banco ao invés de 0 ou 1
    @Column(nullable = false)
    private Role role;
    
    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}