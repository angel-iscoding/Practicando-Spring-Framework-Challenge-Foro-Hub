package com.alura.challenge.foro.controller;

import com.alura.challenge.foro.topico.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/topicos")
public class TopicoController {

    @Autowired
    private TopicoRepository topicoRepository;

    @PostMapping
    @Transactional
    public ResponseEntity<DatosRespuestaTopico> registrarTopico(
            @RequestBody @Valid DatosRegistroTopico datosRegistroTopico,
            UriComponentsBuilder uriComponentsBuilder) {
        
        // Verificar si ya existe un tópico con el mismo título y mensaje
        if (topicoRepository.existsByTituloAndMensaje(datosRegistroTopico.titulo(), datosRegistroTopico.mensaje())) {
            return ResponseEntity.badRequest().build();
        }
        
        Topico topico = topicoRepository.save(new Topico(datosRegistroTopico));

        DatosRespuestaTopico datosRespuestaTopico = new DatosRespuestaTopico(topico);
        
        URI url = uriComponentsBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        
        return ResponseEntity.created(url).body(datosRespuestaTopico);
    }

    @GetMapping
    public ResponseEntity<Page<DatosListadoTopico>> listarTopicos(
            @PageableDefault(size = 10, sort = "fechaCreacion", direction = Sort.Direction.ASC) Pageable paginacion,
            @RequestParam(required = false) String curso,
            @RequestParam(required = false) Integer year) {
        
        Page<DatosListadoTopico> pagina;
        
        if (curso != null && year != null) {
            // Buscar por curso y año
            pagina = topicoRepository.findByCursoAndYear(curso, year, paginacion)
                    .map(DatosListadoTopico::new);
        } else if (curso != null) {
            // Buscar solo por curso
            pagina = topicoRepository.findByCursoContainingIgnoreCase(curso, paginacion)
                    .map(DatosListadoTopico::new);
        } else if (year != null) {
            // Buscar solo por año
            pagina = topicoRepository.findByYear(year, paginacion)
                    .map(DatosListadoTopico::new);
        } else {
            // Listar todos
            pagina = topicoRepository.findAll(paginacion)
                    .map(DatosListadoTopico::new);
        }
        
        return ResponseEntity.ok(pagina);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DatosRespuestaTopico> retornarDatosTopico(@PathVariable Long id) {
        Optional<Topico> topico = topicoRepository.findById(id);
        
        if (topico.isPresent()) {
            var datosTopico = new DatosRespuestaTopico(topico.get());
            return ResponseEntity.ok(datosTopico);
        }
        
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<DatosRespuestaTopico> actualizarTopico(
            @PathVariable Long id,
            @RequestBody @Valid DatosActualizacionTopico datosActualizacionTopico) {
        
        Optional<Topico> topicoOpt = topicoRepository.findById(id);
        
        if (topicoOpt.isPresent()) {
            Topico topico = topicoOpt.get();
            
            // Verificar si existe otro tópico con el mismo título y mensaje
            if (datosActualizacionTopico.titulo() != null && datosActualizacionTopico.mensaje() != null) {
                boolean existeOtroTopico = topicoRepository.existsByTituloAndMensaje(
                        datosActualizacionTopico.titulo(), 
                        datosActualizacionTopico.mensaje());
                
                // Si existe y no es el mismo tópico que estamos actualizando
                if (existeOtroTopico && !topico.getTitulo().equals(datosActualizacionTopico.titulo()) 
                    && !topico.getMensaje().equals(datosActualizacionTopico.mensaje())) {
                    return ResponseEntity.badRequest().build();
                }
            }
            
            topico.actualizarDatos(datosActualizacionTopico);
            
            var datosRespuesta = new DatosRespuestaTopico(topico);
            return ResponseEntity.ok(datosRespuesta);
        }
        
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> eliminarTopico(@PathVariable Long id) {
        Optional<Topico> topico = topicoRepository.findById(id);
        
        if (topico.isPresent()) {
            topicoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        
        return ResponseEntity.notFound().build();
    }
}
