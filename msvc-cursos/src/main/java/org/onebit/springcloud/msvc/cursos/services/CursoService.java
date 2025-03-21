package org.onebit.springcloud.msvc.cursos.services;

import org.onebit.springcloud.msvc.cursos.models.Usuario;
import org.onebit.springcloud.msvc.cursos.models.entity.Curso;

import java.util.List;
import java.util.Optional;

public interface CursoService {
    List<Curso> listar();
    Optional<Curso> porId(Long id);
    Curso guardar(Curso curso);
    void eliminar(Long id);

    // metodos remotos
    Optional<Usuario> asignarUsuario(Usuario usuario, Long cursoId);
    Optional<Usuario> crearUsuario(Usuario usuario, Long cursoId);
    Optional<Usuario> desasignarUsuario(Usuario usuario, Long cursoId);
}
