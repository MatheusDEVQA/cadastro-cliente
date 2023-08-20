package helpers;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties
public class PropertiesUtils {
    @Value("${}")
    private String appUrl;
    @Value("${appUrlEmail}")
    private String appUrlEmail;
    @Value("${useProxy}")
    private Boolean useProxy;
    @Value("${ProxyUrl}")
    private String ProxyUrl;
    @Value("${client_id}")
    private String client_id;

}
