package br.com.restaurante.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record DadosCadastroMesa(
        @NotNull(message = "O número da mesa é obrigatório")
        Integer numero,

        @NotNull(message = "A capacidade é obrigatória")
        @Positive(message = "A capacidade deve ser maior que zero")
        Integer capacidade
) {}