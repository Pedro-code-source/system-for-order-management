package br.com.restaurante.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DadosCadastroIngrediente(
        @NotBlank String nome,
        @NotNull Integer quantidade,
        String unidadeMedida
) {
}
