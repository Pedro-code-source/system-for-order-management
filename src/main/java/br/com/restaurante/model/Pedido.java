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
@MappedSuperclass
public abstract class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Setter
    @NotNull
    private Double valorFinal;

    @Setter
    @NotNull
    private StatusPedido status;

    @Setter
    @NotNull
    private FormaPagamento formaDePagamento;

    @Setter
    @NotEmpty
    private List<ItemCardapio> itens;

    public Pedido(Double valorFinal, StatusPedido status, FormaPagamento formaDePagamento, List<ItemCardapio> itens) {
        this.valorFinal = valorFinal;
        this.status = status;
        this.formaDePagamento = formaDePagamento;
        this.itens = itens;
    }
}
