package com.alura.challenge.foro.controller;

import com.alura.challenge.foro.usuario.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/register")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping
    @Transactional
    public ResponseEntity<DatosRespuestaUsuario> registrarUsuario(
            @RequestBody @Valid DatosRegistroUsuario datosRegistroUsuario,
            UriComponentsBuilder uriComponentsBuilder) {
        
        // Verificar si ya existe un usuario con el mismo email
        if (usuarioRepository.existsByLogin(datosRegistroUsuario.email())) {
            return ResponseEntity.badRequest().build();
        }
        
        // Crear usuario con contraseña encriptada
        Usuario usuario = new Usuario(
                null,
                datosRegistroUsuario.email(),
                passwordEncoder.encode(datosRegistroUsuario.password())
        );
        
        usuario = usuarioRepository.save(usuario);
        
        DatosRespuestaUsuario datosRespuesta = new DatosRespuestaUsuario(usuario);
        
        URI url = uriComponentsBuilder.path("/register/{id}").buildAndExpand(usuario.getId()).toUri();
        
        return ResponseEntity.created(url).body(datosRespuesta);
    }
}
