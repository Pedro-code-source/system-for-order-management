package br.com.restaurante.model;

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
@Table (name = "ingredientes")
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
    private int quantidade;

    private String unidadeMedida = "G";

    @ManyToMany
    @JoinTable(name = "itens_ingrediente",
            joinColumns = @JoinColumn(name = "ingrediente_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id"))
    private List<ItemCardapio> itens;

    @OneToMany(mappedBy = "ingrediente")
    private List<MovimentacaoDeEstoque> movimentacoes;

    public Ingrediente(String nome, int quantidade) {
        this.nome = nome;
        this.quantidade = quantidade;
    }
}
