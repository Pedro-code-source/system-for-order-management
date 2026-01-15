package br.com.restaurante.services;

import br.com.restaurante.model.Endereco;
import br.com.restaurante.repository.EnderecoRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
@Transactional
public class EnderecoService {

    private final EnderecoRepository enderecoRepository;

    public void salvar(Endereco endereco){
        enderecoRepository.save(endereco);
    }

    public void deletarPorId(Endereco endereco) {

        Optional<Endereco> id = enderecoRepository.findById(endereco.getId());
        if (id != null) {
            enderecoRepository.delete(endereco);
        } else {
            throw new RuntimeException();
    }
    }

    public void listarTodos(){
       List<Endereco> lista = enderecoRepository.findAll();
    }

    public void buscarPorId(Long id){
        Optional<Endereco> endereco = enderecoRepository.findById(id);
    }

    public void atualizar(Endereco endereco){
        enderecoRepository.flush();
    }


}
