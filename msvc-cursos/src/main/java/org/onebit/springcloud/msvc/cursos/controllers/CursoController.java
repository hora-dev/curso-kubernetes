package org.onebit.springcloud.msvc.cursos.controllers;

import feign.FeignException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.onebit.springcloud.msvc.cursos.models.Usuario;
import org.onebit.springcloud.msvc.cursos.models.entity.Curso;
import org.onebit.springcloud.msvc.cursos.services.CursoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class CursoController {

    private final CursoService service;

    @GetMapping
    public ResponseEntity<List<Curso>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detalle(@PathVariable Long id) {
        return //service.porId(id)
                service.porIdConUsuarios(id)
                .map(curso -> ResponseEntity.ok(curso))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody Curso curso, BindingResult result) {
        if(result.hasErrors()) {
            return validar(result);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(curso));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@PathVariable Long id, @RequestBody Curso curso, BindingResult result) {
        return service.porId(id)
                .map(usr -> {
                    if(result.hasErrors()) {
                        return validar(result);
                    }
                    usr.setNombre(curso.getNombre());
                    return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(usr));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        return service.porId(id)
                .map(usr -> {
                    service.eliminar(id);
                    return ResponseEntity.noContent().build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    private static ResponseEntity<Map<String, String>> validar(BindingResult result) {
        Map<String, String> errores = new HashMap<>();
        result.getFieldErrors().forEach(err -> {
            errores.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errores);
    }

    @PutMapping("/asignar-usuario/{cursoId}")
    public ResponseEntity<?> asignarUsuario(@PathVariable Long cursoId, @RequestBody Usuario usuario) {
        try {
            return service.asignarUsuario(usuario, cursoId)
                    .map(usr -> ResponseEntity.status(HttpStatus.CREATED).body(usr))
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (FeignException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("mensaje", "No se pudo crear el usuario " +
                            "o error en la comunicación con el servicio de usuarios " +
                            e.getMessage()));
        }
    }

    @PostMapping("/crear-usuario/{cursoId}")
    public ResponseEntity<?> crearUsuario(@PathVariable Long cursoId, @RequestBody Usuario usuario) {
        try {
            return service.crearUsuario(usuario, cursoId)
                    .map(usr -> ResponseEntity.status(HttpStatus.CREATED).body(usr))
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (FeignException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("mensaje", "No se pudo crear el usuario " +
                            "o error en la comunicación con el servicio de usuarios " +
                            e.getMessage()));
        }
    }

    @DeleteMapping("/eliminar-usuario/{cursoId}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Long cursoId, @RequestBody Usuario usuario) {
        try {
            return service.desasignarUsuario(usuario, cursoId)
                    .map(usr -> ResponseEntity.ok(usr))
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (FeignException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("mensaje", "No existe el usuario " +
                            "o error en la comunicación con el servicio de usuarios " +
                            e.getMessage()));
        }
    }

    @DeleteMapping("/eliminar-curso-usuario/{id}")
    public ResponseEntity<?> eliminarCursoUsuarioPorId(@PathVariable Long id) {
        service.eliminarCursoUsuarioPorId(id);
        return ResponseEntity.noContent().build();
    }

}
