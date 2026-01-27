package br.com.restaurante.dtos;

import br.com.restaurante.model.Cliente;
import br.com.restaurante.model.ItemCardapio;
import br.com.restaurante.model.PedidoOnline;

import java.util.List;

public record DadosListagemCliente(Long id, String nome, String email, String telefone) {

    public DadosListagemCliente(Cliente cliente) {
        this(cliente.getId(), cliente.getNome(), cliente.getEmail(), cliente.getTelefone());
    }
}