package br.com.restaurante.dtos;

import br.com.restaurante.model.Endereco;

public record DadosCadastroEndereco(
        String cep,
        String rua,
        String numero,
        String bairro,
        String cidade
) {
    public DadosCadastroEndereco(Endereco endereco) {
        this(endereco.getCep(), endereco.getRua(), endereco.getNumero(), endereco.getBairro(), endereco.getCidade());
    }
}