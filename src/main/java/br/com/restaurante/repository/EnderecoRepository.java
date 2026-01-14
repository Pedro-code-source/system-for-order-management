package br.com.restaurante.repository;

import br.com.restaurante.model.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnderecoRepository extends JpaRepository <Endereco, Long> {
}
