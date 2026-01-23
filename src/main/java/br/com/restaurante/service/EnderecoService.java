package br.com.restaurante.service;

import br.com.restaurante.model.Endereco;
import br.com.restaurante.repository.EnderecoRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class EnderecoService {

    private final EnderecoRepository enderecoRepository;

    @Transactional
    public Endereco salvar(Endereco endereco) {

        endereco.setBairro(endereco.getBairro().trim());
        endereco.setRua(endereco.getRua().trim());

        return enderecoRepository.save(endereco);
    }

    @Transactional
    public void deletarPorId(Long id) {
        if (!enderecoRepository.existsById(id)) {
            throw new RuntimeException("Endereço não encontrado para deletar.");
        }
        enderecoRepository.deleteById(id);
    }

    public List<Endereco> listarTodos() {
        return enderecoRepository.findAll();
    }

    public Endereco buscarPorId(Long id) {
        return enderecoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Endereço não encontrado com ID: " + id));
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
