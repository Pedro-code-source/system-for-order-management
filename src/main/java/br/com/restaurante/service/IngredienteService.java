package br.com.restaurante.service;

import br.com.restaurante.model.Ingrediente;
import br.com.restaurante.repository.IngredienteRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class IngredienteService {
    private final IngredienteRepository ingredienteRepository;

    @Transactional
    public Ingrediente salvar(Ingrediente objeto) {
        return ingredienteRepository.save(objeto);
    }

    public List<Ingrediente> listarTodos() {
        return ingredienteRepository.findAll();
    }

    public Ingrediente buscarPorId(Long id) {
        return ingredienteRepository.findById(id).orElseThrow(() -> new RuntimeException("Ingrediente não encontrado"));
    }

    @Transactional
    public void deletarPorId(Long id) {
        ingredienteRepository.deleteById(id);
    }
}