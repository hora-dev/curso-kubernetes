package org.onebit.springcloud.msvc.cursos.repositories;

import org.onebit.springcloud.msvc.cursos.entity.Curso;
import org.springframework.data.repository.CrudRepository;

public interface CursoRepository extends CrudRepository<Curso, Long> {
}