package br.com.restaurante.dtos;

import br.com.restaurante.model.Entrega;
import br.com.restaurante.model.enums.StatusPedido;

public record DadosListagemEntrega(
        Long idEntrega,
        Long idPedido,
        String nomeCliente,
        String enderecoCompleto,
        StatusPedido statusPedido
) {
    public DadosListagemEntrega(Entrega entrega) {
        this(
                entrega.getId(),
                entrega.getPedidoOnline().getId(),
                entrega.getPedidoOnline().getCliente().getNome(),
                entrega.getEndereco().getRua() + ", " + entrega.getEndereco().getNumero(),
                entrega.getPedidoOnline().getStatus()
        );
    }
}