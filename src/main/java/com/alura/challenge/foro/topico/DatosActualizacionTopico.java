package com.alura.challenge.foro.topico;

public record DatosActualizacionTopico(
        Long id,
        String titulo,
        String mensaje,
        String autor,
        String curso
) {}
