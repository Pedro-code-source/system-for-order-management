package br.com.restaurante.model;

import br.com.restaurante.model.enums.CategoriaItem;
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
public class ItemCardapio {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Setter
    @NotBlank
    private String nome;

    @Setter
    @NotNull
    private Double preco;

    @Setter
    @NotBlank
    private String descricao;

    @Setter
    @NotNull
    private CategoriaItem categoria;

    @Setter
    private String urlFoto;

    public ItemCardapio(String nome, Double preco, String descricao, CategoriaItem categoria, String urlFoto) {
        this.nome = nome;
        this.preco = preco;
        this.descricao = descricao;
        this.categoria = categoria;
        this.urlFoto = urlFoto;
    }
}
