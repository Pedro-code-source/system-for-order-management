package br.com.restaurante.repository;

import br.com.restaurante.model.Administrador;
import br.com.restaurante.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdministradorRepository extends JpaRepository <Administrador, Long> {

    boolean existsByEmail(String email);

    boolean existsBySenha(String senha);

}
