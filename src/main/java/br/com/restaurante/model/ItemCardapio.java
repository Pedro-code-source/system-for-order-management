package br.com.restaurante.model;

import br.com.restaurante.dtos.DadosCadastroItem;
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

    public ItemCardapio(DadosCadastroItem dados) {
        this.nome = dados.nome();
        this.preco = dados.preco();
        this.descricao = dados.descricao();
        this.categoria = dados.categoria();
        this.urlFoto = dados.urlFoto();
    }
}
