package br.com.restaurante.repository;

import br.com.restaurante.model.Entrega;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntregaRepository extends JpaRepository <Entrega, Long> {
}
