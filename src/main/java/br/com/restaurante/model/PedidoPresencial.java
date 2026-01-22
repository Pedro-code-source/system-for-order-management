package br.com.restaurante.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "pedidoPresencial")
public class PedidoPresencial extends Pedido {

    @NotNull(message = "A mesa é obrigatória para pedidos presenciais")
    @ManyToOne
    @JoinColumn(name = "mesa_id", nullable = false)
    private Mesa mesa;

    @Setter
    @ManyToOne
    @JoinColumn(name = "garcom_id", nullable = true)
    private Garcom garcom;
}