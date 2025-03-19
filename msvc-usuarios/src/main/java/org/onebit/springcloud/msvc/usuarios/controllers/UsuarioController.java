package org.onebit.springcloud.msvc.usuarios.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.onebit.springcloud.msvc.usuarios.models.entity.Usuario;
import org.onebit.springcloud.msvc.usuarios.services.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService service;

    @GetMapping
    public List<Usuario> listar() {
        return service.listar();
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
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(usuario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@PathVariable Long id, @Valid @RequestBody Usuario usuario, BindingResult result) {
        return service.porId(id)
                .map(usr -> {
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

    private static ResponseEntity<Map<String, String>> validar(BindingResult result) {
        Map<String, String> errores = new HashMap<>();
        result.getFieldErrors().forEach(err -> {
            errores.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errores);
    }
}
