package com.cadastrocliente.cadastroclienteapp.helpers;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Data
@Configuration
@ConfigurationProperties
public class LoadProperties {

    @Value("${urlSts}")
    private String urlSts;

    @Value("${xMathFlowId}")
    private String xMathFlowId;

    @Value("${x-Math-CorrelationId}")
    private String xMathCorrelationId;

    @Value("${clientId}")
    private String clientId;

    @Value("${useProxy}")
    private String useProxy;

    @Value("${urlMailing}")
    private String urlMailing;

    @Value("${spring.profiles.active:Unknown}")
    private  String activeProfile;
}
