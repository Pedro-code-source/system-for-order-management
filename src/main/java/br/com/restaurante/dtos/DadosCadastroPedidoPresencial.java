package br.com.restaurante.dtos;

import br.com.restaurante.model.Garcom;
import br.com.restaurante.model.ItemCardapio;
import br.com.restaurante.model.Mesa;
import br.com.restaurante.model.enums.FormaPagamento;

import java.util.List;

public record DadosCadastroPedidoPresencial(
        Mesa mesa,
        Garcom garcom,
        FormaPagamento formaPagamento,
        List<ItemCardapio> itens
) {
}
