package br.com.restaurante.model;

import br.com.restaurante.model.enums.UnidadeMedida;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Entity
public class Ingrediente {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Setter
    @NotBlank
    private String nome;

    @Setter
    @NotNull
    private Float quantidade;

    @Setter
    @NotNull
    private UnidadeMedida unidadeMedida;

    public Ingrediente(String nome, Float quantidade, UnidadeMedida unidadeMedida) {
        this.nome = nome;
        this.quantidade = quantidade;
        this.unidadeMedida = unidadeMedida;
    }
}
