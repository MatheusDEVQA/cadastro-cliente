package com.cadastrocliente.cadastroclienteapp.service.impl;

import com.cadastrocliente.cadastroclienteapp.exceptions.BusinessExceptions;
import com.cadastrocliente.cadastroclienteapp.exceptions.InternalErrorException;
import com.cadastrocliente.cadastroclienteapp.helpers.LoadProperties;
import com.cadastrocliente.cadastroclienteapp.model.entity.Cliente;
import com.cadastrocliente.cadastroclienteapp.repository.ClienteRepository;
import com.cadastrocliente.cadastroclienteapp.repository.MailingRepository;
import com.cadastrocliente.cadastroclienteapp.service.ClienteService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import javax.validation.Validation;
import javax.validation.Validator;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteServiceImpl implements ClienteService {

    private ClienteRepository repository;

    private MailingRepository mailingRepository;

    @Autowired
    LoadProperties props;

    public  ClienteServiceImpl(ClienteRepository repository, MailingRepository mailingRepository){
        this.repository=repository;
        this.mailingRepository=mailingRepository;
    }

    @Override
    public Cliente save(Cliente cliente) {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        if (validator.validate(cliente).isEmpty() == false){
            throw new InternalErrorException("Cliente possui campos obrigatórios não preenchidos!");

        }
        try{
            if (repository.existsByUsuario(cliente.getUsuario())){
                throw new BusinessExceptions("Cliente já cadastrado");
            }
            Cliente clienteSalvo = repository.save(cliente);
            try {
                mailingRepository.sendEmail(clienteSalvo.getUsuario());
            }catch (JsonProcessingException e){
                throw  new BusinessExceptions("Erro ao enviar email");
            }
            return clienteSalvo;
        }catch (DataAccessException e){
            System.out.println(e);
            throw new InternalErrorException("Erro ao acessar bancos de dados", e);
        }
    }

    @Override
    public List<Cliente> getList(){
        try{
            return repository.findAll();
        }catch (DataAccessException e){
            throw new InternalErrorException("Erro ao acessar o BD!", e);
        }
    }

    @Override
    public Optional<Cliente> getById(long id){
        try{
            return this.repository.findById(id);

        }catch (DataAccessException e){
            throw new InternalErrorException("Erro ao acessar BD", e);
        }
    }


}
