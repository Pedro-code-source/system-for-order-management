package br.com.restaurante.repository;

import br.com.restaurante.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservaRepository extends JpaRepository <Reserva, Long> {
}
