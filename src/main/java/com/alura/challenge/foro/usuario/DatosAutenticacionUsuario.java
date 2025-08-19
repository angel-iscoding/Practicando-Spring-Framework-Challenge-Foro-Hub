package com.alura.challenge.foro.usuario;

import jakarta.validation.constraints.NotBlank;

public record DatosAutenticacionUsuario(
        @NotBlank
        String login,
        @NotBlank
        String password
) {
}
