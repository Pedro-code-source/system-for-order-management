package br.com.restaurante.dtos;

import br.com.restaurante.model.Mesa;
import br.com.restaurante.model.enums.StatusMesa;

public record DadosListagemMesa(
        Long id,
        Integer numero,
        Integer capacidade,
        StatusMesa status
) {
    public DadosListagemMesa(Mesa mesa) {
        this(
                mesa.getId(),
                mesa.getNumero(),
                mesa.getCapacidade(),
                mesa.getStatus()
        );
    }
}