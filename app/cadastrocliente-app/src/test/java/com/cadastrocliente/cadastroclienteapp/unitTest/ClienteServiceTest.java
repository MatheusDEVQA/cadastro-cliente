package com.cadastrocliente.cadastroclienteapp.unitTest;

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
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@WebMvcTest
public class ClienteServiceTest {

    static String CLIENTE_API = "/clientes";

    @MockBean
    ClienteService service;

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;


    @Test
    @DisplayName("Cliente valido")
    void post_newCliente_returnCreated() throws Exception {
        //PREPARAÇÃO
        Cliente clienteToSave = ClienteMockFactory.novoClienteMath();
        Cliente clienteAfterSave = ClienteMockFactory.clienteCadastradoMathId9();

        BDDMockito.given(service.save(clienteToSave)).willReturn(clienteAfterSave);

        ClienteDTO clienteDTOToCreate = ClienteDTOMockFactory.novoClienteMath();
        ClienteDTO expectRwsult = ClienteDTOMockFactory.clienteCdastradoMathId9();

        String json = objectMapper.writeValueAsString(clienteDTOToCreate);

        //EXECUÇÃO
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post(CLIENTE_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();

        //VALIDAÇÃO
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());

        String contentAsString = response.getContentAsString(StandardCharsets.UTF_8);
        ClienteDTO clienteResponse = objectMapper.readValue(contentAsString, ClienteDTO.class);
        assertEquals(expectRwsult, clienteResponse);

        Mockito.verify(service, Mockito.times(1)).save(clienteToSave);

    }
}
