package br.com.restaurante.dtos;

import br.com.restaurante.model.ItemCardapio;
import br.com.restaurante.model.enums.CategoriaItem;

public record DadosListagemItem (Long id, String nome, String descricao, Double preco, CategoriaItem categoriaItem, String urlFoto) {

    public DadosListagemItem(ItemCardapio itemCardapio) {
        this(itemCardapio.getId(), itemCardapio.getNome(), itemCardapio.getDescricao(), itemCardapio.getPreco(), itemCardapio.getCategoria(), itemCardapio.getUrlFoto());
    }
}
