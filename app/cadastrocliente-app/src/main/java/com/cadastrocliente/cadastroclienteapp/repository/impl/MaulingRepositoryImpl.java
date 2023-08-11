package com.cadastrocliente.cadastroclienteapp.repository.impl;

import com.cadastrocliente.cadastroclienteapp.exceptions.BusinessExceptions;
import com.cadastrocliente.cadastroclienteapp.helpers.LoadProperties;
import com.cadastrocliente.cadastroclienteapp.helpers.StsToken;
import com.cadastrocliente.cadastroclienteapp.model.dto.EmailDTO;
import com.cadastrocliente.cadastroclienteapp.model.dto.ReponseEmailDTO;
import com.cadastrocliente.cadastroclienteapp.repository.MailingRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.ProxySpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.useRelaxedHTTPSValidation;

@Repository
public class MailingRepositoryImpl implements MailingRepository {

    public MailingRepositoryImpl(){

    }
    @Autowired
    LoadProperties properties;

    StsToken stsToken;

    Response response;

    @Override
    public ReponseEmailDTO sendEmail(String email) {
        stsToken = new StsToken(properties);
        useRelaxedHTTPSValidation();
        if (Boolean.parseBoolean(properties.getUseProxy())) {
            String proxyUrl = "proxy.math";
            int proxyPort = 8080;
            RestAssured.proxy = ProxySpecification.host(proxyUrl).withPort(proxyPort);
        }
        EmailDTO clenteEmail = new EmailDTO().builder().email(email).build();
        try {
            String emailJson = new ObjectMapper().writeValueAsString(clenteEmail);
            response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
                    .relaxedHTTPSValidation()
                    .header("x-math-apikey", "1")
                    .header("x-math-correlationID", UUID.randomUUID().toString())
                    .header("Authorization", stsToken.getToken())
                    .body(emailJson).when()
                    .post(properties.getUrlMailing());
        } catch (BusinessExceptions | JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
