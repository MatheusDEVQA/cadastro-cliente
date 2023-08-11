package com.cadastrocliente.cadastroclienteapp.repository;

import com.cadastrocliente.cadastroclienteapp.model.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    boolean existsByUsuario (String usuario);
}
