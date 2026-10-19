
package com.scarfox.jack.config;

import com.scarfox.jack.repository.AgenteRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class AgenteAtivoFilter extends OncePerRequestFilter {

    private final AgenteRepository agenteRepository;

    public AgenteAtivoFilter(AgenteRepository agenteRepository) {
        this.agenteRepository = agenteRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) 
    throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof JwtAuthenticationToken jwtToken) {
            String keycloakId = jwtToken.getToken().getSubject();
            if (agenteRepository.findByKeycloakIdAndAtivoTrue(keycloakId).isEmpty()) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Agente inativo ou não cadastrado.");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
