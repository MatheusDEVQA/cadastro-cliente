package com.cadastrocliente.cadastroclienteapp.helpers;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.ProxySpecification;
import lombok.Data;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.useRelaxedHTTPSValidation;

@Data
public class StsToken {

    public String token;

    LoadProperties properties;

    public  StsToken(LoadProperties properties){
        this.properties= properties;
        try{
            GeraToken(properties);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void GeraToken(LoadProperties props){
        JsonPath resp = null;
        useRelaxedHTTPSValidation();
        if(Boolean.parseBoolean(props.getUseProxy())){
            String proxyUrl = "proxynew.math";
            int proxyPort = 8080;
            RestAssured.proxy = ProxySpecification.host(proxyUrl).withPort(proxyPort);
        }


    }
}
