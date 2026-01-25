package br.com.restaurante.dtos;

public record DadosCadastroCliente(
        String nome,
        String email,
        String telefone,
        String senha,
        DadosEndereco endereco
) {}