package br.com.restaurante.service;

import br.com.restaurante.dtos.DadosCadastroEndereco;
import br.com.restaurante.model.Endereco;
import br.com.restaurante.repository.EnderecoRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@AllArgsConstructor
@Service
public class EnderecoService {

    private final EnderecoRepository enderecoRepository;

    @Transactional
    public Endereco salvar(DadosCadastroEndereco dto) {
        Endereco endereco = new Endereco(dto);
        return enderecoRepository.save(endereco);
    }

    public Endereco buscarPorId(Long id) {
        return enderecoRepository.findById(id).orElseThrow(() -> new RuntimeException("Endereço não encontrado"));
    }

    @Transactional
    public Endereco atualizar(Long id, Endereco enderecoAtualizado) {
        Endereco enderecoExistente = buscarPorId(id);

        enderecoExistente.setRua(enderecoAtualizado.getRua());
        enderecoExistente.setNumero(enderecoAtualizado.getNumero());
        enderecoExistente.setCep(enderecoAtualizado.getCep());
        enderecoExistente.setBairro(enderecoAtualizado.getBairro());
        enderecoExistente.setCidade(enderecoAtualizado.getCidade());

        return enderecoRepository.save(enderecoExistente);
    }
}
