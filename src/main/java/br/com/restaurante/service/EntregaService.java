package br.com.restaurante.service;

import br.com.restaurante.model.Entrega;
import br.com.restaurante.repository.EntregaRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class EntregaService {

    private final EntregaRepository entregaRepository;

    @Transactional
    public Entrega salvar(Entrega entrega){
        return entregaRepository.save(entrega);
    }

    @Transactional
    public void deletarPorId(Long id) {
        if (!entregaRepository.existsById(id)){
            throw new RuntimeException("Entrega não encontrada para ser deletada.");
        } else {
            entregaRepository.deleteById(id);
        }
    }

    @Transactional
    public List<Entrega> listarTodos(){
        return entregaRepository.findAll();
    }

    @Transactional
    public Entrega buscarPorId (Long id){
        return entregaRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Entrega não encontrada."));
    }
}