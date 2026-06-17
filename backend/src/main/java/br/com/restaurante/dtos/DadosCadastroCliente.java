package br.com.restaurante.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DadosCadastroCliente(
        @NotBlank(message = "O nome é obrigatório")
        String nome,
        @NotBlank(message = "O e-mail é obrigatório")
        String email,
        @NotBlank(message = "O telefone é obrigatório")
        String telefone,
        @NotBlank(message = "A senha é obrigatória")
        String senha,
        @NotNull(message = "O endereço é obrigatório")
        DadosCadastroEndereco endereco
) {}