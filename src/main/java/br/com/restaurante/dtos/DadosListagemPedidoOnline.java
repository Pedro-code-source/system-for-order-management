package br.com.restaurante.dtos;

import br.com.restaurante.model.ItemCardapio;
import br.com.restaurante.model.Pedido;
import br.com.restaurante.model.PedidoOnline;
import br.com.restaurante.model.PedidoPresencial;
import br.com.restaurante.model.enums.FormaPagamento;
import br.com.restaurante.model.enums.StatusPedido;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record DadosListagemPedidoOnline(
        Long id,
        LocalDateTime dataHora,
        StatusPedido status,
        Double valorTotal,
        FormaPagamento formaPagamento,
        List<String> itens
) {

    public DadosListagemPedidoOnline(PedidoOnline pedido) {
        this(
                pedido.getId(),
                pedido.getDataHora(),
                pedido.getStatus(),
                pedido.getValorFinal(),
                pedido.getFormaDePagamento(),
                pedido.getItens().stream().map(ItemCardapio::getNome).collect(Collectors.toList())
                );
    }
}
