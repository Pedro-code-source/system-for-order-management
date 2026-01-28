package br.com.restaurante.model;

import br.com.restaurante.dtos.DadosCadastroAdmin;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "administrador")
public class Administrador extends Usuario {

    private String nome;

    public Administrador(DadosCadastroAdmin dados) {
        super(dados.email(), dados.senha());
        this.nome = dados.nome();
    }
}