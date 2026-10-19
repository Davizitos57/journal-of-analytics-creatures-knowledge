package com.scarfox.jack.service;

import com.scarfox.jack.config.KeycloakAdminProperties;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;
import java.util.Map;

@Service
public class KeycloakAdminService {

    private final RestClient restClient;
    private final KeycloakAdminProperties properties;

    public KeycloakAdminService(RestClient.Builder restClientBuilder, KeycloakAdminProperties properties) {
        this.restClient = restClientBuilder.baseUrl(properties.url()).build();
        this.properties = properties;
    }

    public String obterTokenAdministrativo() {
        MultiValueMap<String, String> formulario = new LinkedMultiValueMap<>();
        formulario.add("grant_type", "client_credentials");

        Map<?, ?> resposta = restClient.post()
                .uri("/realms/{realm}/protocol/openid-connect/token", properties.realm())
                .headers(headers -> headers.setBasicAuth(properties.clientId(), properties.clientSecret()))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(formulario)
                .retrieve()
                .body(Map.class);

        if (resposta == null || !(resposta.get("access_token") instanceof String token)) {
            throw new IllegalStateException("O Keycloak não retornou um token administrativo.");
        }

        return token;
    }

    public String criarUsuario(String email, String senha) {
        Map<String, Object> usuario = Map.of(
            "username", email,
            "email", email,
            "enabled", true,
            "emailVerified", false,
            "credentials", List.of(Map.of(
                "type", "password",
                "value", senha,
                "temporary", false
            ))
        );

        ResponseEntity<Void> resposta;

        try {
            resposta = restClient.post()
                .uri("/admin/realms/{realm}/users", properties.realm())
                .headers(headers -> headers.setBearerAuth(obterTokenAdministrativo()))
                .contentType(MediaType.APPLICATION_JSON)
                .body(usuario)
                .retrieve()
                .toBodilessEntity();
        } catch (HttpClientErrorException.Conflict erro) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe uma conta cadastrada com esse email ou nome de usuário.", erro);
        }

        URI location = resposta.getHeaders().getLocation();

        if (location == null) {
            throw new IllegalStateException("O Keycloak não retornou o identificador do usuário.");
        }

        return location.getPath().substring(location.getPath().lastIndexOf('/') + 1);
    }

    public void alterarStatusUsuario(String keycloakId, boolean habilitado) {
        restClient.put()
                .uri("/admin/realms/{realm}/users/{id}", properties.realm(), keycloakId)
                .headers(headers -> headers.setBearerAuth(obterTokenAdministrativo()))
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("enabled", habilitado))
                .retrieve()
                .toBodilessEntity();
    }

    public void excluirUsuario(String keycloakId) {
        restClient.delete()
            .uri("/admin/realms/{realm}/users/{id}", properties.realm(), keycloakId)
            .headers(headers -> headers.setBearerAuth(obterTokenAdministrativo()))
            .retrieve()
            .toBodilessEntity();
    }
}