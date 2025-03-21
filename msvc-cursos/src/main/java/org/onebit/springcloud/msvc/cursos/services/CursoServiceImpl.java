package org.onebit.springcloud.msvc.cursos.services;

import lombok.RequiredArgsConstructor;
import org.onebit.springcloud.msvc.cursos.clients.UsuarioClientRest;
import org.onebit.springcloud.msvc.cursos.models.Usuario;
import org.onebit.springcloud.msvc.cursos.models.entity.Curso;
import org.onebit.springcloud.msvc.cursos.repositories.CursoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CursoServiceImpl implements CursoService {

    private final CursoRepository repository;
    private final UsuarioClientRest clientRest;

    @Override
    @Transactional(readOnly = true)
    public List<Curso> listar() {
        return (List<Curso>) repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Curso> porId(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional
    public Curso guardar(Curso curso) {
        return repository.save(curso);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Optional<Usuario> asignarUsuario(Usuario usuario, Long cursoId) {
        return Optional.empty();
    }

    @Override
    public Optional<Usuario> crearUsuario(Usuario usuario, Long cursoId) {
        return Optional.empty();
    }

    @Override
    public Optional<Usuario> desasignarUsuario(Usuario usuario, Long cursoId) {
        return Optional.empty();
    }
}
