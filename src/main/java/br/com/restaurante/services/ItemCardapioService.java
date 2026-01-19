package br.com.restaurante.services;

import br.com.restaurante.model.ItemCardapio;
import br.com.restaurante.repository.ItemCardapioRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ItemCardapioService {
    private final ItemCardapioRepository itemRepository;

    @Transactional
    public ItemCardapio salvar(ItemCardapio objeto) {
        return itemRepository.save(objeto);
    }

    @Transactional
    public List<ItemCardapio> listarTodos() {
        return itemRepository.findAll();
    }

    @Transactional
    public ItemCardapio buscarPorId(Long id) {
        return itemRepository.findById(id).orElseThrow(() -> new RuntimeException("Item não encontrado"));
    }

    @Transactional
    public void deletarPorId(Long id) {
        itemRepository.deleteById(id);
    }
}