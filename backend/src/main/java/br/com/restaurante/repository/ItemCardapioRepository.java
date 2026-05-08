package br.com.restaurante.repository;

import br.com.restaurante.model.ItemCardapio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemCardapioRepository extends JpaRepository <ItemCardapio, Long> {
}
