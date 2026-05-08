package br.com.restaurante.dtos;

import br.com.restaurante.model.Administrador;

public record DadosListagemAdmin(Long id, String nome, String email) {

    public DadosListagemAdmin(Administrador administrador) {
        this(administrador.getId(), administrador.getNome(), administrador.getEmail());
    }
}