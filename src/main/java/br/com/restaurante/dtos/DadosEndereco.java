package br.com.restaurante.dtos;

import br.com.restaurante.model.Endereco;
import jakarta.validation.constraints.NotNull;

public record DadosEndereco(
        String cep,
        String rua,
        String numero,
        String bairro,
        String cidade
) {
    public DadosEndereco(Endereco endereco) {
        this(endereco.getCep(), endereco.getRua(), endereco.getNumero(), endereco.getBairro(), endereco.getCidade());
    }
}