package com.alura.challenge.foro.topico;

import jakarta.validation.constraints.NotNull;

public record DatosActualizacionTopico(
        @NotNull
        Long id,
        String titulo,
        String mensaje,
        String autor,
        String curso
) {}
