package br.com.restaurante.dtos;

import br.com.restaurante.model.Entrega;
import br.com.restaurante.model.enums.FormaPagamento;

public record DadosCadastroPedidoOnline(
        Entrega entrega,
        FormaPagamento formaPagamento
) {
}
