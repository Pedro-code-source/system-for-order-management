package br.com.restaurante.dtos;

import br.com.restaurante.model.enums.CategoriaItem;

public record DadosCadastroItem(
        String nome,
        Double preco,
        String descricao,
        CategoriaItem categoria,
        String urlFoto
) {

}
