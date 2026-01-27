package br.com.restaurante.dtos;

import br.com.restaurante.model.Endereco;
import br.com.restaurante.model.PedidoOnline;

public record DadosCadastroEntrega(
        Endereco endereco,
        PedidoOnline pedidoOnline
) {
}
