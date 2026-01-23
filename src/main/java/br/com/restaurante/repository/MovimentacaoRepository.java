package br.com.restaurante.repository;

import br.com.restaurante.model.MovimentacaoDeEstoque;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovimentacaoRepository extends JpaRepository <MovimentacaoDeEstoque, Long> {
}
