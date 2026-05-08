package br.com.restaurante.repository;

import br.com.restaurante.model.PedidoOnline;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoOnlineRepository extends JpaRepository <PedidoOnline, Long> {
}
