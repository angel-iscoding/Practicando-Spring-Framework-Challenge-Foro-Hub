package com.alura.challenge.foro.topico;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TopicoRepository extends JpaRepository<Topico, Long> {
    boolean existsByTituloAndMensaje(String titulo, String mensaje);

    Page<Topico> findByCursoContainingIgnoreCase(String curso, Pageable pageable);

    @Query("SELECT t FROM Topico t WHERE YEAR(t.fechaCreacion) = :year")
    Page<Topico> findByYear(@Param("year") int year, Pageable pageable);

    @Query("SELECT t FROM Topico t WHERE t.curso LIKE %:curso% AND YEAR(t.fechaCreacion) = :year")
    Page<Topico> findByCursoAndYear(@Param("curso") String curso, @Param("year") int year, Pageable pageable);
}
