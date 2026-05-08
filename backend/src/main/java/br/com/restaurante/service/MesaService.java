package br.com.restaurante.service;

import br.com.restaurante.model.Mesa;
import br.com.restaurante.model.enums.StatusMesa;
import br.com.restaurante.repository.MesaRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class MesaService {
    private final MesaRepository mesaRepository;

    @Transactional
    public Mesa salvar(Mesa objeto) {
        return mesaRepository.save(objeto);
    }

    @Transactional
    public List<Mesa> listarTodos() {
        return mesaRepository.findAll();
    }

    @Transactional
    public Mesa buscarPorId(Long id) {
        return mesaRepository.findById(id).orElseThrow(() -> new RuntimeException("Mesa não encontrada"));
    }

    @Transactional
    public void deletarPorId(Long id) {
        mesaRepository.deleteById(id);
    }

}
