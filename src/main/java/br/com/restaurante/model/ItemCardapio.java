package br.com.restaurante.model;

import br.com.restaurante.model.enums.CategoriaItem;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Entity
@Table (name = "itens")
public class ItemCardapio {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Setter
    @NotBlank(message = "O nome do item é obrigatório")
    @Column(nullable = false)
    private String nome;

    @Setter
    @NotNull(message = "O preço é obrigatório")
    @Column(nullable = false)
    private Double preco;

    @Setter
    @NotBlank(message = "A descrição é obrigatória")
    @Column(nullable = false)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Setter
    @NotNull(message = "A categoria é obrigatória")
    @Column(nullable = false)
    private CategoriaItem categoria;

    @Setter
    private String urlFoto;

    @ManyToMany(mappedBy = "itens")
    private List<Pedido> pedidos;

    @ManyToMany(mappedBy = "itens")
    private List<Ingrediente> ingredientes;

    public ItemCardapio(String nome, Double preco, String descricao, CategoriaItem categoria, String urlFoto) {
        this.nome = nome;
        this.preco = preco;
        this.descricao = descricao;
        this.categoria = categoria;
        this.urlFoto = urlFoto;
    }
}
