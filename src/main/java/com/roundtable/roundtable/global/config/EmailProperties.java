package com.roundtable.roundtable.global.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import java.util.Properties;

@Getter
@ConfigurationProperties(prefix = "spring.mail")
public class EmailProperties {
    private String host;
    private int port;
    private String username;
    private String password;
    private Properties properties;

    public EmailProperties(String host, int port, String username, String password, Properties properties) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.properties = properties;
    }
}
