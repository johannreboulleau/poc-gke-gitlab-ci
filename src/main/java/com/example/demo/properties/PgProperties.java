package com.example.demo.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "pg")
@Data
public class PgProperties {

    private String jdbcUrl;
    private String username;
    private String password;
    private String instanceName;
}
