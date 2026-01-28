package br.com.restaurante.model;

import br.com.restaurante.dtos.DadosCadastroIngrediente;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "ingredientes")
public class Ingrediente {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Setter
    @NotBlank(message = "O nome do ingrediente é obrigatório")
    @Column(nullable = false)
    private String nome;

    @Setter
    @NotNull(message = "A quantidade inicial é obrigatória")
    @Column(nullable = false)
    private Integer quantidade;

    @Setter
    private String unidadeMedida = "G";

    public Ingrediente(DadosCadastroIngrediente dados) {
        this.nome = dados.nome();
        this.quantidade = dados.quantidade();
        this.unidadeMedida = dados.unidadeMedida();
    }
}