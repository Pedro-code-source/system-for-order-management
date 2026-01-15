package br.com.restaurante.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class PedidoOnline extends Pedido {

    @NotNull(message = "A entrega é obrigatória para pedidos online")
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "entrega_id", nullable = false)
    private Entrega entrega;
}