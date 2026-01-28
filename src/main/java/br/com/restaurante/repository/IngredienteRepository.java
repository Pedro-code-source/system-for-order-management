package br.com.restaurante.repository;

import br.com.restaurante.model.Ingrediente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredienteRepository extends JpaRepository <Ingrediente, Long> {
    boolean existsByNome(String nome);
}
