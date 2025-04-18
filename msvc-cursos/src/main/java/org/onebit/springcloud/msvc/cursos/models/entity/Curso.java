package org.onebit.springcloud.msvc.cursos.models.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.onebit.springcloud.msvc.cursos.models.Usuario;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cursos")
@Getter
@Setter
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String nombre;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "curso_id")
    private List<CursoUsuario> cursoUsuarios;

    @Transient
    private List<Usuario> usuarios;


    public Curso() {
        this.cursoUsuarios = new ArrayList<>();
        this.usuarios = new ArrayList<>();
    }

    public void addCursoUsuario(CursoUsuario cursoUsuario) {
        this.cursoUsuarios.add(cursoUsuario);
    }

    public void removeCursoUsuario(CursoUsuario cursoUsuario) {
        this.cursoUsuarios.remove(cursoUsuario);
    }
}
