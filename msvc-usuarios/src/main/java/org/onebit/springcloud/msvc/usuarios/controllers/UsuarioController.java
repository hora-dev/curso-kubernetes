package org.onebit.springcloud.msvc.usuarios.controllers;

import lombok.RequiredArgsConstructor;
import org.onebit.springcloud.msvc.usuarios.models.entity.Usuario;
import org.onebit.springcloud.msvc.usuarios.services.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<?> crear(@RequestBody Usuario usuario) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(usuario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@PathVariable Long id, @RequestBody Usuario usuario) {
        return service.porId(id)
                .map(usr -> {
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
}
