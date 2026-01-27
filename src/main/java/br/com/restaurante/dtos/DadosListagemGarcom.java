package br.com.restaurante.dtos;

import br.com.restaurante.model.Garcom;

public record DadosListagemGarcom(
        Long id,
        String nome
) {
    public DadosListagemGarcom(Garcom garcom){
        this(
                garcom.getId(),
                garcom.getNome()
        );
    }
}
