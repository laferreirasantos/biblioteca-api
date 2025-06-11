package com.pge.biblioteca.infrastructure.persistence;

import com.pge.biblioteca.domain.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    boolean existsByMatricula(String matricula);

    Optional<Usuario> findByMatricula(String matricula);
}
