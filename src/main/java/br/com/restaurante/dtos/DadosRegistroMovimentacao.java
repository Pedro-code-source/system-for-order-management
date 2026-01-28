package br.com.restaurante.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record DadosRegistroMovimentacao(
        @NotNull(message = "O ID do ingrediente é obrigatório")
        Long idIngrediente,

        @NotNull(message = "A quantidade é obrigatória")
        @Positive(message = "A quantidade deve ser maior que zero")
        Integer quantidade
) {}