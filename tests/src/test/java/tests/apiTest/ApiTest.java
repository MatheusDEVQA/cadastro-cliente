package tests.apiTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.ClienteDTO;
import helpers.ClienteRequests;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ApiTest {


    @Test
    void post_createdClient_returnCreated() throws JsonProcessingException {

        ClienteDTO clienteDTO = ClienteRequests.clienteGenarate();
        String clienteJson = new ObjectMapper().writeValueAsString(clienteDTO);

        //EXECUÇÃO
        given().accept(ContentType.JSON).contentType(ContentType.JSON)
                .header("x-apikey", "")
                .header("x-correlationID", "")
                .header("Authorization", "")
                .body(clienteJson)
                .when().post("url clientes")
                .then()


                //VALIDAÇÃO
                .statusCode(HttpStatus.SC_CREATED);
    }

    @Test
    void post_createdClientCadastrado_returnBadRequest() throws JsonProcessingException {

        ClienteDTO clienteDTO = ClienteRequests.clienteGenarate();
        String clienteJson = new ObjectMapper().writeValueAsString(clienteDTO);


        given().accept(ContentType.JSON).contentType(ContentType.JSON)
                .header("x-apikey", "")
                .header("x-correlationID", "")
                .header("Authorization", "")
                .body(clienteJson)
                .when().post("url clientes");

        //EXECUÇÃO
        Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
                .header("x-apikey", "")
                .header("x-correlationID", "")
                .header("Authorization", "")
                .body(clienteJson)
                .when().post("url clientes")
                .then()


                //VALIDAÇÃO
                .statusCode(HttpStatus.SC_BAD_REQUEST).extract().response();
        assertEquals("Cliente já cadastrado", response.jsonPath().getString("errors"));

    }

    @Test
    void post_invalidClient_returnBadRequest() throws JsonProcessingException {

        ClienteDTO clienteDTO = new ClienteDTO();


        //EXECUÇÃO
        Response reponse = given().accept(ContentType.JSON).contentType(ContentType.JSON)
                .header("x-apikey", "")
                .header("x-correlationID", "")
                .header("Authorization", "")
                .body(clienteDTO)
                .when().post("url clientes")
                .then()


                //VALIDAÇÃO
                .statusCode(HttpStatus.SC_BAD_REQUEST).extract().response();
        String responseError = reponse.jsonPath().getString("errors");
        assertThat(responseError).contains("O nome é obrigatório!");
    }


    @Test
    void post_invalidEmail_returnBadRequest() {

        ClienteDTO clienteDTO = new ClienteDTO().builder()
                .primeiroNome("")
                .usuario("")
                .cidade("")
                .ultimoNome("").build();

        //EXECUÇÃO

        Response reponse =given().accept(ContentType.JSON).contentType(ContentType.JSON)
                .header("x-apikey", "")
                .header("x-correlationID", "")
                .header("Authorization", "")
                .body(clienteDTO)
                .when().post("url clientes")
                .then()


                //VALIDAÇÃO
                .statusCode(HttpStatus.SC_BAD_REQUEST).extract().response();
        String responseError = reponse.jsonPath().getString("errors");
        assertThat(responseError).contains("Formato de e-mail inválido!");
    }

    @Test
    void get_clientList_returnOK(){
        given().accept(ContentType.JSON).contentType(ContentType.JSON)
                .header("x-apikey", "")
                .header("x-correlationID", "")
                .header("Authorization", "")
                .when().get("url clientes")
                .then()

                //VALIDAÇÃO
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    void get_clientList_returnUnauthorized(){
        given().accept(ContentType.JSON).contentType(ContentType.JSON)
                .header("x-apikey", "")
                .header("x-correlationID", "")
                .when().get("url clientes")
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED);
    }

    @Test
    void get_getById_returnNotFOund(){
        given().accept(ContentType.JSON).contentType(ContentType.JSON)
                .header("x-apikey", "")
                .header("x-correlationID", "")
                .header("Authorization", "")
                .when().get("url clientes com Id=0")
                .then()

                //VALIDAÇÃO
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    void get_findById_returnOK() throws JsonProcessingException {
        //PREPARAÇÃO
        ClienteDTO clienteDTO = ClienteRequests.clienteGenarate();
        String clienteJson = new ObjectMapper().writeValueAsString(clienteDTO);

        //EXECUÇÃO
        ClienteDTO clienteDTO1 = given().accept(ContentType.JSON).contentType(ContentType.JSON)
                .header("x-apikey", "")
                .header("x-correlationID", "")
                .header("Authorization", "")
                .body(clienteJson)
                .when().post("url clientes")
                .then().extract().as(ClienteDTO.class);

        long id = clienteDTO1.getId();

        //EXECUÇÃO
        given().accept(ContentType.JSON).contentType(ContentType.JSON)
                .header("x-apikey", "")
                .header("x-correlationID", "")
                .header("Authorization", "")
                .when().get("url clientes com id")
                .then()

                //VALIDAÇÃO
                .statusCode(HttpStatus.SC_OK);

    }
}
