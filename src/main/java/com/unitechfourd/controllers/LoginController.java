package com.unitechfourd.controllers;

import com.unitechfourd.controllers.dto.LoginRequestDTO;
import com.unitechfourd.security.jwt.JWTUtils;
import com.unitechfourd.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final JWTUtils jwtUtils;

    @Autowired
    public LoginController(AuthenticationManager authenticationManager, JWTUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequestDTO loginRequestDTO) {
        // Autentica o usuário usando o gerenciador de autenticação com as credenciais fornecidas
        var authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDTO.username(), loginRequestDTO.password()));
        // Define o contexto de segurança com a autenticação bem-sucedida
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // Obtém os detalhes do usuário autenticado
        var userDetails = (UserDetailsImpl) authentication.getPrincipal();
        // Gera o cookie JWT com base nos detalhes do usuário autenticado
        var jwtCookie = jwtUtils.generateJwtCookie(userDetails);
        // Retorna uma resposta bem-sucedida com o cabeçalho HTTP contendo o cookie JWT
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString()).body(null);
    }
}