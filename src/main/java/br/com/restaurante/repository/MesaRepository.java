package br.com.restaurante.repository;

import br.com.restaurante.model.Mesa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MesaRepository extends JpaRepository <Mesa, Long> {
}
