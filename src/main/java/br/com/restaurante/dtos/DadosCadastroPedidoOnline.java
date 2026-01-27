package br.com.restaurante.dtos;

import br.com.restaurante.model.Entrega;
import br.com.restaurante.model.enums.FormaPagamento;
import br.com.restaurante.model.enums.StatusPedido;

import java.util.List;

public record DadosCadastroPedidoOnline(
        Long clienteId,
        List<Long> itensIds,
        FormaPagamento formaPagamento,
        StatusPedido status
) {
}
