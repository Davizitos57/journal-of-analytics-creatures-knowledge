package com.scarfox.jack.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "keycloak.admin")
public record KeycloakAdminProperties(String url, String realm, String clientId, String clientSecret) {
    
}