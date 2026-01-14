package br.com.restaurante.repository;

import br.com.restaurante.model.Garcom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GarcomRepository extends JpaRepository <Garcom, Long> {
}
