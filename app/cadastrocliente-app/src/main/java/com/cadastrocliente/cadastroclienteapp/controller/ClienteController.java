package com.cadastrocliente.cadastroclienteapp.controller;

import com.cadastrocliente.cadastroclienteapp.model.dto.ClienteDTO;
import com.cadastrocliente.cadastroclienteapp.model.entity.Cliente;
import com.cadastrocliente.cadastroclienteapp.service.ClienteService;
import javax.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ClienteController {

    private ClienteService service;

    private ModelMapper modelMapper;

    @InitBinder("cliente")
    public  void initClienteBinder(WebDataBinder binder){
        binder.setDisallowedFields("Id");
    }

    public ClienteController(ClienteService service, ModelMapper modelMapper) {
        this.service = service;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/clientes")
    @ResponseStatus(HttpStatus.CREATED)
    public ClienteDTO createCliente(@RequestBody @Valid ClienteDTO cliente) {
        Cliente entity = modelMapper.map(cliente, Cliente.class);
        entity = service.save(entity);
        return modelMapper.map(entity, ClienteDTO.class);

    }

    @GetMapping("/clientes")
    @ResponseStatus(HttpStatus.OK)
    public List<ClienteDTO> getCliente() {
        List<Cliente> clienteList = this.service.getList();
        return clienteList.stream().map(cliente -> modelMapper.map(cliente, ClienteDTO.class)).collect(Collectors.toList());

    }

    @GetMapping("/clientes/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ClienteDTO getClienteId (@PathVariable long id){
        return service
                .getById(id)
                .map(cliente -> modelMapper.map(cliente, ClienteDTO.class))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    }
}
