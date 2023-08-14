package com.cadastrocliente.cadastroclienteapp.helpers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class ConfigurationApplication implements ApplicationRunner {

    @Autowired
    LoadProperties properties;

    @Override
    public  void run(ApplicationArguments args) throws Exception{
        if(Boolean.parseBoolean((properties.getUseProxy()))){
            // print golden ticket

        }
        this.properties = properties;
    }
}
