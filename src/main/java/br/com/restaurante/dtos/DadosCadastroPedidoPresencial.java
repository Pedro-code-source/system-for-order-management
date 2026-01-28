package br.com.restaurante.dtos;

import br.com.restaurante.model.Garcom;
import br.com.restaurante.model.ItemCardapio;
import br.com.restaurante.model.Mesa;
import br.com.restaurante.model.enums.FormaPagamento;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record DadosCadastroPedidoPresencial(
        @NotNull Mesa mesa,
        @NotNull Garcom garcom,
        @NotNull FormaPagamento formaPagamento,
        @NotNull List<ItemCardapio> itens
) {}