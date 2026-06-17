package br.com.restaurante.dtos;

import br.com.restaurante.model.Endereco;
import jakarta.validation.constraints.NotBlank;

public record DadosCadastroEndereco(
        @NotBlank(message = "O CEP é obrigatório")
        String cep,
        @NotBlank(message = "A rua é obrigatória")
        String rua,
        @NotBlank(message = "O número é obrigatório")
        String numero,
        @NotBlank(message = "O bairro é obrigatório")
        String bairro,
        @NotBlank(message = "A cidade é obrigatória")
        String cidade
) {
    public DadosCadastroEndereco(Endereco endereco) {
        this(endereco.getCep(), endereco.getRua(), endereco.getNumero(), endereco.getBairro(), endereco.getCidade());
    }
}