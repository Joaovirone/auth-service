package com.finance.auth_service.domain.model;

/**
 * Enum que representa os Papéis (Roles) de acesso dentro do sistema.
 * * O QUE É UM ENUM?
 * É um tipo especial do Java que define um conjunto fixo de constantes.
 * Garantimos que o usuário só pode ter exatamente um desses tipos, 
 * evitando erros de digitação como "Admn", "client", etc.


 * * POR QUE O PREFIXO 'ROLE_'?
 * O Spring Security possui um padrão (convention over configuration).
 * Quando usamos métodos como .hasRole("ADMIN"), o framework procura automaticamente
 * no banco de dados ou no token por uma string que seja "ROLE_ADMIN".
 * Manter esse prefixo facilita a integração automática com a segurança.
 */

public enum Role {
    
    /**
     * Administrador do sistema.
     * Tem permissão total para gerenciar usuários, carteiras e configurações.
     */
    ROLE_ADMIN,

    /**
     * Cliente final (Usuário comum).
     * Tem permissão apenas para gerenciar seus próprios dados e carteira.
     */
    ROLE_CLIENT
}