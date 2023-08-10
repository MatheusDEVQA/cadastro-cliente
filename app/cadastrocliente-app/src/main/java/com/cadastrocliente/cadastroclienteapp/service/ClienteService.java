package com.cadastrocliente.cadastroclienteapp.service;

import com.cadastrocliente.cadastroclienteapp.model.entity.Cliente;

import java.util.List;

public interface ClienteService {

    Cliente save(Cliente any);

    List<Cliente> getList();




}
