package br.com.restaurante.model;

import br.com.restaurante.dtos.DadosCadastroGarcom;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Entity
@Getter
@Table(name = "garcom")
public class Garcom extends Usuario {

    @Setter
    @NotBlank(message = "O nome do garçom é obrigatório")
    @Column(nullable = false)
    private String nome;

    public Garcom(DadosCadastroGarcom dados) {
        super(dados.email(), dados.senha());
        this.nome = dados.nome();
    }
}