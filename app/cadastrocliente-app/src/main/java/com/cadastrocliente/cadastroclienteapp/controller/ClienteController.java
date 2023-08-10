package com.cadastrocliente.cadastroclienteapp.controller;

import com.cadastrocliente.cadastroclienteapp.model.dto.ClienteDTO;
import com.cadastrocliente.cadastroclienteapp.model.entity.Cliente;
import com.cadastrocliente.cadastroclienteapp.service.ClienteService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.RequestBody;

public class ClienteController {

    private ClienteService service;

    private ModelMapper modelMapper;

    public ClienteController(ClienteService service, ModelMapper modelMapper){
        this.service = service;
        this.modelMapper = modelMapper;
    }

    public ClienteDTO createCliente(@RequestBody @Valid ClienteDTO cliente){
        Cliente entity = modelMapper.map(cliente, Cliente.class);
        entity = service.save(entity);
        return modelMapper.map(entity, ClienteDTO.class);

    }
}
