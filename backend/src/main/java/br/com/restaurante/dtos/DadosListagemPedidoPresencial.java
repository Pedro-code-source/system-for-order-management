package br.com.restaurante.dtos;

import br.com.restaurante.model.PedidoPresencial;
import br.com.restaurante.model.enums.FormaPagamento;
import br.com.restaurante.model.enums.StatusPedido;

import java.time.LocalDateTime;

public record DadosListagemPedidoPresencial(Long id,
                                            Double valorFinal,
                                            StatusPedido status,
                                            FormaPagamento formaPagamento,
                                            LocalDateTime dataHora,
                                            Integer numeroMesa,
                                            String nomeGarcom) {

    public DadosListagemPedidoPresencial(PedidoPresencial pedido) {
        this(
                pedido.getId(),
                pedido.getValorFinal(),
                pedido.getStatus(),
                pedido.getFormaDePagamento(),
                pedido.getDataHora(),
                pedido.getMesa().getNumero(),
                pedido.getGarcom() != null ? pedido.getGarcom().getNome() : null
        );
    }
}
