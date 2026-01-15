package com.finance.auth_service.infrastructure.persistance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

import com.finance.auth_service.infrastructure.persistance.entity.UserEntity;

/**
 * @Repository
 * O QUE FAZ: Marca esta interface como um componente de acesso a dados (DAO).
 * TÉCNICO: Além de injetá-la no Spring, essa anotação ativa a "tradução de exceções".
 * Se o banco (Postgres) lançar um erro bizarro de SQL, o Spring captura e lança
 * uma exceção padronizada do Spring (ex: DataIntegrityViolationException),
 * facilitando o tratamento de erros.
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    
    /**
     * EXTENDS JpaRepository<UserEntity, UUID>
     * * O QUE É: Herança mágica do Spring Data.
     * Ao estender essa interface, ganhamos de graça métodos prontos para CRUD:
     * - save(entity) -> INSERT ou UPDATE
     * - findById(uuid) -> SELECT por ID
     * - delete(entity) -> DELETE
     * - findAll() -> SELECT *
     * * <UserEntity, UUID>: Dizemos ao Spring: "Gerencie a tabela da classe UserEntity
     * onde a chave primária é do tipo UUID".
     */

    /**
     * Busca um usuário pelo email.
     * * O QUE FAZ: Query Method (Método de Consulta Derivado).
     * O Spring lê o nome do método "findByEmail" e gera o SQL automaticamente:
     * SQL GERADO: SELECT * FROM tb_users WHERE email = ?
     * * @param email O email a ser buscado.
     * @return Optional<UserEntity>: Retorna um container que pode ter o usuário ou estar vazio.
     * Por que Optional? Evita o famoso erro NullPointerException. Se não achar,
     * tratamos o vazio (.orElseThrow) de forma elegante.
     */
    Optional<UserEntity> findByEmail(String email);

    /**
     * Verifica se já existe um usuário cadastrado com este email.
     * * O QUE FAZ: Otimização de busca.
     * Ao invés de trazer todos os dados do usuário (o que seria pesado),
     * o banco faz uma verificação rápida (geralmente SELECT 1 ou COUNT).
     * * ONDE USAR: Validação no momento do cadastro (Register).
     * Antes de salvar, chamamos isso para garantir que o email é único.
     * * @param email O email para verificação.
     * @return true se existir, false se estiver livre.
     */
    boolean existsByEmail(String email);
}