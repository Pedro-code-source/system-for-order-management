package br.com.restaurante.dtos;

import br.com.restaurante.model.Pedido;
import br.com.restaurante.model.PedidoOnline;
import br.com.restaurante.model.PedidoPresencial;
import br.com.restaurante.model.enums.StatusPedido;

import java.time.LocalDateTime;
import java.util.List;

public record DadosListagemPedido(
        Long id,
        String nomeCliente,
        List<DadosListagemItem> itens,
        Double valorTotal,
        StatusPedido status,
        LocalDateTime dataHora,
        Integer numeroMesa,
        DadosEndereco endereco
) {

    public DadosListagemPedido(Pedido pedido) {
        this(
                pedido.getId(),
                pedido.getCliente().getNome(),
                pedido.getItens().stream().map(DadosListagemItem::new).toList(),
                pedido.getValorFinal(),
                pedido.getStatus(),
                pedido.getDataHora(),
                (pedido instanceof PedidoPresencial p) ? p.getMesa().getNumero() : null,
                (pedido instanceof PedidoOnline p) ? new DadosEndereco(p.getEntrega().getEndereco()) : null
        );
    }
}
