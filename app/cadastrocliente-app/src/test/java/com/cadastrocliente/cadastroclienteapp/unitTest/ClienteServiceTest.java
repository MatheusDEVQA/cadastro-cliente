package com.cadastrocliente.cadastroclienteapp.unitTest;

import com.cadastrocliente.cadastroclienteapp.exceptions.BusinessExceptions;
import com.cadastrocliente.cadastroclienteapp.exceptions.InternalErrorException;
import com.cadastrocliente.cadastroclienteapp.helpers.ClienteDTOMockFactory;
import com.cadastrocliente.cadastroclienteapp.helpers.ClienteMockFactory;
import com.cadastrocliente.cadastroclienteapp.model.dto.ClienteDTO;
import com.cadastrocliente.cadastroclienteapp.model.entity.Cliente;
import com.cadastrocliente.cadastroclienteapp.repository.ClienteRepository;
import com.cadastrocliente.cadastroclienteapp.repository.MailingRepository;
import com.cadastrocliente.cadastroclienteapp.service.ClienteService;
import com.cadastrocliente.cadastroclienteapp.service.impl.ClienteServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@ExtendWith(SpringExtension.class)
public class ClienteServiceTest {

    ClienteService service;

    @MockBean
    ClienteRepository repository;

    @MockBean
    MailingRepository mailingRepository;

    @BeforeEach
    public void setUp() {
        this.service = new ClienteServiceImpl(repository, mailingRepository);
    }

    @Test
    @DisplayName("Cliente valido salvo deve retornar cliente salvo com Id")
    void save_newCliente_returnCreatedWhitId() {
        //PREPARAÇÃO
        Cliente cliente = ClienteMockFactory.novoClienteMath();
        String usuario = cliente.getUsuario();

        BDDMockito.when(repository.existsByUsuario(usuario)).thenReturn(false);

        Cliente clienteCadastrado = ClienteMockFactory.clienteCadastradoMathId9();
        BDDMockito.when(repository.save(cliente)).thenReturn(clienteCadastrado);

        //EXECUÇÃO
        Cliente clienteResponse = service.save(cliente);

        //VALIDAÇÃO
        assertEquals(clienteCadastrado, clienteResponse);

        Mockito.verify(repository, Mockito.times(1)).existsByUsuario(usuario);
        Mockito.verify(repository, Mockito.times(1)).save(cliente);

    }

    @Test
    @DisplayName("Cliente valido salvo deve retornar cliente salvo com Id")
    void save_duplicateCliente_returnBusinessExceptionWhithMessageJaCadastardo() {
        //PREPARAÇÃO
        Cliente cliente = ClienteMockFactory.novoClienteMath();
        String usuario = cliente.getUsuario();

        BDDMockito.when(repository.existsByUsuario(usuario)).thenReturn(true);


        //EXECUÇÃO
        Throwable exception = catchThrowable(() -> service.save(cliente));

        //VALIDAÇÃO
        assertThat(exception).isInstanceOf(BusinessExceptions.class);
        assertThat(exception).hasMessage("Cliente já cadastrado");

        Mockito.verify(repository, Mockito.times(1)).existsByUsuario(usuario);
        Mockito.verify(repository, Mockito.never()).save(cliente);

    }

    @Test
    @DisplayName("O metodo save deve lançar exceção quando tentar cadastrar com campo obrigatorio não preenchido")
    void save_newClienteFieldsError_returnInternalErroCamposInvalidos() {
        //PREPARAÇÃO
        Cliente cliente = new Cliente();

        //EXECUÇÃO
        Throwable exception = catchThrowable(() -> service.save(cliente));

        //VALIDAÇÃO
        assertThat(exception).isInstanceOf(InternalErrorException.class).hasMessage("Cliente possui campos obrigatórios não preenchidos!");

        Mockito.verify(repository, Mockito.never()).existsByUsuario(cliente.getUsuario());
        Mockito.verify(repository, Mockito.never()).save(cliente);

    }

    @Test
    @DisplayName("O metodo save não deve tartar exceção de sql lançada pelo repository ao tentar salvar cliente")
    void save_existByUserError_returnInternalErrorErroConsultarBD() {
        //PREPARAÇÃO
        Cliente cliente = ClienteMockFactory.novoClienteMath();
        String usuario = cliente.getUsuario();

        BDDMockito.when(repository.existsByUsuario(usuario)).thenThrow(new JpaSystemException(new InternalErrorException("Erro ao acessar o banco de dados")));

        //EXECUÇÃO
        Throwable exception = catchThrowable(() -> service.save(cliente));

        //VALIDAÇÃO
        assertTrue(exception instanceof InternalErrorException);
        assertEquals(exception.getMessage(), "Erro ao acessar bancos de dados");

        Mockito.verify(repository, Mockito.times(1)).existsByUsuario(usuario);
        Mockito.verify(repository, Mockito.never()).save(cliente);

    }

    @Test
    @DisplayName("O metodo save não deve lançar exceção de sql lançada pelo repository ao tentar verificar se o cliente existe")
    void save_existByUserError_returnInternalErrorConsultarBD() {
        //PREPARAÇÃO
        Cliente cliente = ClienteMockFactory.novoClienteMath();
        String usuario = cliente.getUsuario();

        BDDMockito.when(repository.existsByUsuario(usuario)).thenThrow(new JpaSystemException(new RuntimeException("Database offline")));

        //EXECUÇÃO
        Throwable exception = catchThrowable(() -> service.save(cliente));

        //VALIDAÇÃO
        assertTrue(exception instanceof InternalErrorException);
        assertEquals(exception.getMessage(), "Erro ao acessar bancos de dados");

        Mockito.verify(repository, Mockito.times(1)).existsByUsuario(usuario);
        Mockito.verify(repository, Mockito.never()).save(cliente);

    }

    @Test
    @DisplayName("O metodo save deve enviar exceção ao tentar enviar email de boas vinda ao salvar cliente com sucesso")
    void exceptionSendEMailSaveNewCLient() throws JsonProcessingException {
        //PREPARAÇÃO
        Cliente cliente = ClienteMockFactory.novoClienteMath();
        String usuario = cliente.getUsuario();

        BDDMockito.when(repository.existsByUsuario(usuario)).thenReturn(false);
        Cliente ClienteCadastrado = ClienteMockFactory.clienteCadastradoMathId9();
        BDDMockito.when(repository.save(cliente)).thenReturn(ClienteCadastrado);
        BDDMockito.when(mailingRepository.sendEmail(usuario)).thenThrow(new InternalErrorException("Erro enviar email de boas vindas"));

        //EXECUÇÃO
        Throwable exception = catchThrowable(() -> service.save(cliente));

        //VALIDAÇÃO
        assertThat(exception).isInstanceOf(InternalErrorException.class);
        assertThat(exception).hasMessage("Erro enviar email de boas vindas");
        assertTrue(exception instanceof InternalErrorException);
        Mockito.verify(mailingRepository, Mockito.times(1)).sendEmail(usuario);
    }

    @Test
    @DisplayName("O metodo findById deve retornar o cliente obtido pelo repository")
    void findId_foundClienteId_returnClienteRegistred() {
        //PREPARAÇÃO
        Cliente cliente = ClienteMockFactory.clienteCadastradoMathId9();
        long id = cliente.getId();

        BDDMockito.when(repository.findById(id)).thenReturn(Optional.of(cliente));

        //EXECUÇÃO
        Optional<Cliente> foundCliente = service.getById(id);

        //VALIDAÇÃO
        assertTrue(foundCliente.isPresent());
        assertEquals(cliente, foundCliente.get());

        Mockito.verify(repository, Mockito.times(1)).findById(id);

    }

    @Test
    @DisplayName("O metodo findList deve retornar a lista de clientes obtida pelo repository")
    void findAll_foundAllClients_returnAllClientesRegistred() {
        //PREPARAÇÃO
        Cliente[] clienteList = {ClienteMockFactory.clienteCadastradoMathId9(), ClienteMockFactory.clienteCadastradoMathId10()};

        BDDMockito.when(repository.findAll()).thenReturn(Arrays.asList(clienteList));

        //EXECUÇÃO
        Cliente[] result = service.getList().toArray(Cliente[]::new);

        //VALIDAÇÃO
        assertTrue(Arrays.equals(clienteList, result));

        Mockito.verify(repository, Mockito.times(1)).findAll();

    }

    @Test
    @DisplayName("O metodo finById não deve lançar exceção de sql lançada pelo repository ao tentar verificar se o cliente existe")
    void finById_getIdError_returnInternalErrorConsultarBD() {
        //PREPARAÇÃO
        long id = 1;
        BDDMockito.when(repository.findById(id)).thenThrow(new JpaSystemException(new InternalErrorException("Erro ao acessar o banco de dados")));

        //EXECUÇÃO
        Throwable exception = catchThrowable(() -> service.getById(id));

        //VALIDAÇÃO
        assertTrue(exception instanceof InternalErrorException);
        assertEquals(exception.getMessage(), "Erro ao acessar BD");

        Mockito.verify(repository, Mockito.times(1)).findById(id);
    }

    @Test
    @DisplayName("O metodo finById deve retornar empty caso o repository nao encontre o cliente por id")
    void finById_noExistClienteId_returnMeptyClient() {
        //PREPARAÇÃO
        long id = 99;
        BDDMockito.when(repository.findById(id)).thenReturn(Optional.empty());

        //EXECUÇÃO
        Optional<Cliente> notFound = service.getById(id);

        //VALIDAÇÃO
        assertTrue(notFound.isEmpty());
        Mockito.verify(repository, Mockito.times(1)).findById(id);
    }

    @Test
    @DisplayName("O metodo finById não deve lançar exceção de sql lançada pelo repository ao tentar verificar se o cliente existe")
    void finAll_getAllError_returnInternalErrorConsultarBD() {
        //PREPARAÇÃO
        BDDMockito.when(repository.findAll()).thenThrow(new JpaSystemException(new InternalErrorException("Erro ao acessar o banco de dados")));

        //EXECUÇÃO
        Throwable exception = catchThrowable(() -> service.getList());

        //VALIDAÇÃO
        assertTrue(exception instanceof InternalErrorException);
        assertEquals(exception.getMessage(), "Erro ao acessar o BD!");

        Mockito.verify(repository, Mockito.times(1)).findAll();
    }


}

