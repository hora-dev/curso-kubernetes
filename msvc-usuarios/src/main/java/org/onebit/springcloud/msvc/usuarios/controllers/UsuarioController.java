package org.onebit.springcloud.msvc.usuarios.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.onebit.springcloud.msvc.usuarios.models.entity.Usuario;
import org.onebit.springcloud.msvc.usuarios.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService service;

    @Autowired
    private Environment env;

    @GetMapping
    public ResponseEntity<?> listar() {
        Map<String, Object> body = new HashMap<>();
        body.put("usuarios", service.listar());
        body.put("pod_info", env.getProperty("MY_POD_NAME") + ": " + env.getProperty("MY_POD_IP"));
        return ResponseEntity.ok(body);
        //return Map.of("usuarios", service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detalle(@PathVariable Long id) {
        /*Optional<Usuario> o = service.porId(id);
        if(o.isPresent()) {
            return ResponseEntity.ok(o.get());
        }
        return ResponseEntity.notFound().build();*/

        return service.porId(id)
                .map(usuario -> ResponseEntity.ok(usuario))
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @PostMapping
    //@ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> crear(@Valid @RequestBody Usuario usuario, BindingResult result) {

        if(result.hasErrors()) {
            return validar(result);
        }

        if(service.existePorEmail(usuario.getEmail())) {
            return ResponseEntity.badRequest().body(
                    Map.of("mensaje", "Ya existe un usuario con el email " + usuario.getEmail())
            );
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(usuario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@PathVariable Long id, @Valid @RequestBody Usuario usuario, BindingResult result) {

        return service.porId(id)
                .map(usr -> {

                    if(!usuario.getEmail().equalsIgnoreCase(usr.getEmail()) && service.porEmail(usuario.getEmail()).isPresent()) {
                        return ResponseEntity.badRequest().body(
                                Map.of("mensaje", "Ya existe un usuario con el email " + usuario.getEmail())
                        );
                    }

                    if(result.hasErrors()) {
                        return validar(result);
                    }

                    usr.setNombre(usuario.getNombre());
                    usr.setEmail(usuario.getEmail());
                    usr.setPassword(usuario.getPassword());

                    return ResponseEntity.ok(service.guardar(usr));
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

    @GetMapping("/usuarios-curso")
    public ResponseEntity<List<Usuario>> obtenerAlumnosPorCurso(@RequestParam List<Long> ids) {
        return ResponseEntity.ok(service.listarPorIds(ids));
    }

    private static ResponseEntity<Map<String, String>> validar(BindingResult result) {
        Map<String, String> errores = new HashMap<>();
        result.getFieldErrors().forEach(err -> {
            errores.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errores);
    }
}
