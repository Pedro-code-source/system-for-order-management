package br.com.restaurante.dtos;

import br.com.restaurante.model.Ingrediente;

public record DadosListagemIngrediente(
        Long id,
        String nome,
        Integer quantidade,
        String unidadeMedida
) {

    public DadosListagemIngrediente(Ingrediente ingrediente) {
        this(
                ingrediente.getId(),
                ingrediente.getNome(),
                ingrediente.getQuantidade(),
                ingrediente.getUnidadeMedida()
        );
    }
}
