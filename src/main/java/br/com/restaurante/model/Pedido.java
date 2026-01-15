package br.com.restaurante.model;

import br.com.restaurante.model.enums.FormaPagamento;
import br.com.restaurante.model.enums.StatusPedido;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Setter
    @NotNull(message = "O valor final é obrigatório")
    @Column(nullable = false)
    private Double valorFinal;

    @Setter
    @NotNull(message = "O status do pedido é obrigatório")
    @Column(nullable = false)
    private StatusPedido status;

    @Setter
    @NotNull(message = "A forma de pagamento é obrigatória")
    @Column(nullable = false)
    private FormaPagamento formaDePagamento;

    @Setter
    @NotEmpty(message = "O pedido deve conter pelo menos um item")
    @ManyToMany
    @JoinTable(name = "itens_pedido",
            joinColumns = @JoinColumn(name = "pedido_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id"))
    private List<ItemCardapio> itens;

    @Setter
    @NotNull(message = "O cliente é obrigatório")
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Setter
    @ManyToOne
    @JoinColumn(name = "garcom_id", nullable = true)
    private Garcom garcom;

    public Pedido(Double valorFinal, StatusPedido status, FormaPagamento formaDePagamento, List<ItemCardapio> itens, Cliente cliente) {
        this.valorFinal = valorFinal;
        this.status = status;
        this.formaDePagamento = formaDePagamento;
        this.itens = itens;
        this.cliente = cliente;
    }
}
