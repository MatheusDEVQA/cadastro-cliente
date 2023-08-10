package com.cadastrocliente.cadastroclienteapp.service;

import com.cadastrocliente.cadastroclienteapp.model.entity.Cliente;

import java.util.List;
import java.util.Optional;

public interface ClienteService {

    Cliente save(Cliente any);

    List<Cliente> getList();


    Optional<Cliente> getById(long id);


}
