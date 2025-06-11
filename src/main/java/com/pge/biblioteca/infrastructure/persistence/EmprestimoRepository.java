package com.pge.biblioteca.infrastructure.persistence;

import com.pge.biblioteca.domain.model.Emprestimo;
import com.pge.biblioteca.domain.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {

    List<Emprestimo> findByUsuario(Usuario usuario);

    List<Emprestimo> findByUsuarioAndDataDevolucaoIsNull(Usuario usuario);
    
    List<Emprestimo> findByUsuarioId(Long usuarioId);


}
