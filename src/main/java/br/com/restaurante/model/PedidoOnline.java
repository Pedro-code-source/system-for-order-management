package br.com.restaurante.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "pedidoOnline")
public class PedidoOnline extends Pedido {

    @NotNull(message = "A entrega é obrigatória para pedidos online")
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "entrega_id", nullable = false)
    private Entrega entrega;

}