package br.com.restaurante.repository;

import br.com.restaurante.model.Cliente;
import br.com.restaurante.model.Garcom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GarcomRepository extends JpaRepository <Garcom, Long> {

    boolean existsByEmail(String email);

}
