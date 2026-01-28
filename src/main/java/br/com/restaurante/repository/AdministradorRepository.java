package br.com.restaurante.repository;

import br.com.restaurante.model.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AdministradorRepository extends JpaRepository <Administrador, Long> {

    boolean existsByEmail(String email);

}
