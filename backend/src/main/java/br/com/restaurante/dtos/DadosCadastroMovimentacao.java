package br.com.restaurante.dtos;

import br.com.restaurante.model.enums.TipoMovimentacao;

public record DadosCadastroMovimentacao(
        Long idIngrediente,
        int quantidade,
        TipoMovimentacao tipo
) {}