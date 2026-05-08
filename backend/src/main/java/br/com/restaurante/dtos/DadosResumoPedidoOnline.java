package br.com.restaurante.dtos;

import br.com.restaurante.model.PedidoOnline;
import br.com.restaurante.model.enums.StatusPedido;

import java.time.LocalDateTime;

public record DadosResumoPedidoOnline(
        Long id,
        Double valorFinal,
        StatusPedido status,
        LocalDateTime dataHora
) {
    public DadosResumoPedidoOnline(PedidoOnline pedido) {
        this(
                pedido.getId(),
                pedido.getValorFinal(),
                pedido.getStatus(),
                pedido.getDataHora()
        );
    }
}
