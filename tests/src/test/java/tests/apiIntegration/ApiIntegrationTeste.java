package tests.apiIntegration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.ClienteDTO;
import dto.EmailDTO;
import helpers.ClienteRequests;
import helpers.StsToken;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ApiIntegrationTeste {

    @Test
    void post_email_returnMessage() throws JsonProcessingException {
        //PREPARAÇÃO
        StsToken stsToken = new StsToken();

        ClienteDTO clienteDTO = ClienteRequests.clienteGenarate();
        String clienteJson = new ObjectMapper().writeValueAsString(clienteDTO);

        //EXECUÇÃO
        ClienteDTO clienteResponse = given()
                .contentType(ContentType.JSON)
                .header("x-apikey","")
                .header("x-correlationID","")
                .header("Authorization","")
                .body(clienteJson)
                .when().post("rota clientes")
                .then().contentType(ContentType.JSON)
                .extract().body().as(ClienteDTO.class);

        String email = clienteResponse.getUsuario();

        EmailDTO clienteemail = new EmailDTO().builder()
                .email(email).build();

        Response emailResponse = given()
                .accept(ContentType.JSON).contentType(ContentType.JSON)
                .header("x-apikey","")
                .header("x-correlationID","")
                .header("Authorization","")
                .when().body(email)
                .post("rota emails")
                .then().contentType(ContentType.JSON)
                .extract().response();

        String response = emailResponse.jsonPath().getString("response");
        //VALIDAÇÃO

        assertThat(response).isEqualTo("Email de boas vindos enviado com sucesso para o email " + email);

    }
}
