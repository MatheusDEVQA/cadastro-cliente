package com.cadastrocliente.cadastroclienteapp.unitTest;


import com.cadastrocliente.cadastroclienteapp.exceptions.InternalErrorException;
import com.cadastrocliente.cadastroclienteapp.helpers.ClienteDTOMockFactory;
import com.cadastrocliente.cadastroclienteapp.helpers.ClienteMockFactory;
import com.cadastrocliente.cadastroclienteapp.model.dto.ClienteDTO;
import com.cadastrocliente.cadastroclienteapp.model.entity.Cliente;
import com.cadastrocliente.cadastroclienteapp.service.ClienteService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@WebMvcTest
public class ClienteControllerTest {

    @MockBean
    ClienteService service;

    @Autowired
    ObjectMapper objectMapper;

    static String CLIENTE_API = "/clientes";

    @Autowired
    MockMvc mvc;

    @Test
    @DisplayName("Validar criar usuario")
    void post_newClient_returnsCreated() throws Exception {
        //preparação
        Cliente clienteToSave = ClienteMockFactory.novoClienteMath();
        Cliente clienteAfterSave = ClienteMockFactory.clienteCadastradoMathId9();

        BDDMockito.given(service.save(clienteToSave)).willReturn(clienteAfterSave);

        ClienteDTO clienteDTOToCreate = ClienteDTOMockFactory.novoClienteMath();
        ClienteDTO expectedResult = ClienteDTOMockFactory.clienteCdastradoMathId9();

        String json = objectMapper.writeValueAsString(clienteDTOToCreate);

        // EXECUÇÃO

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post(CLIENTE_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();

        //VALIDAÇÃO

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());

        String contentString = response.getContentAsString(StandardCharsets.UTF_8);
        ClienteDTO clienteResponse = objectMapper.readValue(contentString, ClienteDTO.class);
        assertEquals(expectedResult, clienteResponse);

        Mockito.verify(service, Mockito.times(1)).save(clienteToSave);

    }

    @Test
    @DisplayName("Validar get usuario(id) deve retornar pelo service como dto")
    void getById_clienteCadastrado_returnOk() throws Exception {
        //preparação
        Cliente cliente = ClienteMockFactory.clienteCadastradoMathId9();
        long clienteId = cliente.getId();

        BDDMockito.given(service.getById(clienteId)).willReturn(Optional.of(cliente));

        ClienteDTO expectedResult = ClienteDTOMockFactory.clienteCdastradoMathId9();

        // EXECUÇÃO
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(CLIENTE_API.concat("/" + clienteId))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();

        //VALIDAÇÃO

        assertEquals(HttpStatus.OK.value(), response.getStatus());

        String contentString = response.getContentAsString(StandardCharsets.UTF_8);
        ClienteDTO clienteResponse = objectMapper.readValue(contentString, ClienteDTO.class);
        assertEquals(expectedResult, clienteResponse);

        Mockito.verify(service, Mockito.times(1)).getById(clienteId);

    }

    @Test
    @DisplayName("Validar get clientes deve retornar a lista de clientes retornada pelo service como dto")
    void getAll_clientesCadastrados_returnOk() throws Exception {
        //preparação
        Cliente[] clienteList = {ClienteMockFactory.clienteCadastradoMathId9(), ClienteMockFactory.clienteCadastradoMathId10()};

        BDDMockito.given(service.getList()).willReturn(Arrays.asList(clienteList));

        ClienteDTO[] expectedClientes = {ClienteDTOMockFactory.clienteCdastradoMathId9(), ClienteDTOMockFactory.clienteCdastradoMathId10()};

        // EXECUÇÃO
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(CLIENTE_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();

        //VALIDAÇÃO

        assertEquals(HttpStatus.OK.value(), response.getStatus());

        String contentString = response.getContentAsString(StandardCharsets.UTF_8);
        ClienteDTO[] clienteDTOResponse = objectMapper.readValue(contentString, ClienteDTO[].class);
        assertTrue(Arrays.equals(expectedClientes, clienteDTOResponse));

        Mockito.verify(service, Mockito.times(1)).getList();

    }

    @Test
    @DisplayName("Validar get usuario deve retornar pelo service como dto")
    void post_withInternalServerErrro_status500() throws Exception {
        //preparação
        Cliente clienteToSave = ClienteMockFactory.novoClienteMath();

        BDDMockito.given(service.save(clienteToSave)).willThrow(new InternalErrorException("Erro ao acessar o banco de dados",
                new JpaSystemException(new RuntimeException("Database is offline"))));

        ClienteDTO clienteDTO = ClienteDTOMockFactory.novoClienteMath();

        String json = objectMapper.writeValueAsString(clienteDTO);

        // EXECUÇÃO
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post(CLIENTE_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);
        ResultActions resultActions = mvc.perform(requestBuilder);


        //VALIDAÇÃO
        resultActions.andExpect(status().isInternalServerError())
                .andExpect(jsonPath("errors[0]", equalTo("Erro ao acessar o banco de dados")));

        Mockito.verify(service, Mockito.times(1)).save(clienteToSave);

    }

    @Test
    @DisplayName("Validar get cliente deve retornar 500 quando o banco estiver offline")
    void get_internalError_Status500() throws Exception {
        //preparação
        BDDMockito.given(service.getList()).willThrow(new InternalErrorException("Erro ao acessar o banco de dados",
                new JpaSystemException(new RuntimeException("Database is offline"))));

        // EXECUÇÃO
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(CLIENTE_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        ResultActions resultActions = mvc.perform(requestBuilder);

        //VALIDAÇÃO
        resultActions.andExpect(status().isInternalServerError())
                        .andExpect(jsonPath("errors[0]",equalTo("Erro ao acessar o banco de dados")));

        Mockito.verify(service, Mockito.times(1)).getList();

    }
    @Test
    @DisplayName("Validar get cliente(id) deve retornar erro quando o banco estiver offline")
    void getById_bandoOffline_retun500() throws Exception {
        //preparação
        long clienteId = 1;

        BDDMockito.given(service.getById(clienteId)).willThrow(new InternalErrorException("Erro ao acessar o banco de dados",
                new JpaSystemException(new RuntimeException("Database is offline"))));

        ClienteDTO expectedResult = ClienteDTOMockFactory.clienteCdastradoMathId9();

        // EXECUÇÃO
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(CLIENTE_API.concat("/" + clienteId))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        ResultActions resultActions = mvc.perform(requestBuilder);

        //VALIDAÇÃO
        resultActions.andExpect(status().isInternalServerError())
                        .andExpect(jsonPath("errors[0]",equalTo("Erro ao acessar o banco de dados")));

        Mockito.verify(service, Mockito.times(1)).getById(clienteId);

    }
}
