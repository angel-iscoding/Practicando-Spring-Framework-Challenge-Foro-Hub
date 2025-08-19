package com.alura.challenge.foro.usuario;

public record DatosRespuestaUsuario(
        Long id,
        String email,
        String mensaje
) {
    public DatosRespuestaUsuario(Usuario usuario) {
        this(
            usuario.getId(),
            usuario.getLogin(),
            "Usuario registrado exitosamente"
        );
    }
}
