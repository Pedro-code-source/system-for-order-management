package br.com.restaurante.repository;

import br.com.restaurante.model.PedidoPresencial;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoPresencialRepository extends JpaRepository <PedidoPresencial, Long> {
}
