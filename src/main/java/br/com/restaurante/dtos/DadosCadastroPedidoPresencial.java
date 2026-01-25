package br.com.restaurante.dtos;

import br.com.restaurante.model.Garcom;
import br.com.restaurante.model.Mesa;
import br.com.restaurante.model.enums.FormaPagamento;

public record DadosCadastroPedidoPresencial(
        Mesa mesa,
        Garcom garcom,
        FormaPagamento formaPagamento
) {
}
