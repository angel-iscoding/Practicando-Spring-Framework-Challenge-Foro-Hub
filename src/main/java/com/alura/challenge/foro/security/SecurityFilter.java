package com.alura.challenge.foro.security;

import com.alura.challenge.foro.usuario.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var requestURI = request.getRequestURI();
        
        if ("/login".equals(requestURI) || "/register".equals(requestURI)) {
            filterChain.doFilter(request, response);
            return;
        }
        
        var authHeader = request.getHeader("Authorization");
        
        if (authHeader != null) {
            var token = authHeader.replace("Bearer ", "");
            
            try {
                var subject = tokenService.getSubject(token);
                
                if (subject != null) {
                    var usuario = usuarioRepository.findByLogin(subject);
                    if (usuario != null) {
                        var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            } catch (RuntimeException e) {
                System.out.println("Token JWT inválido o expirado: " + e.getMessage());
            }
        }
        
        filterChain.doFilter(request, response);
    }
}
