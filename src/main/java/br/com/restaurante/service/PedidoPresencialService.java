package br.com.restaurante.service;

import br.com.restaurante.model.PedidoPresencial;
import br.com.restaurante.model.enums.StatusPedido;
import br.com.restaurante.repository.PedidoPresencialRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class PedidoPresencialService {
    private final PedidoPresencialRepository repository;

    @Transactional
    public PedidoPresencial salvar(PedidoPresencial objeto) {
        return repository.save(objeto);
    }

    public List<PedidoPresencial> listarTodos() {
        return repository.findAll();
    }

    public PedidoPresencial buscarPorId(Long id){
        return repository.findById(id).orElseThrow(()-> new RuntimeException("Pedido não encontrado."));
    }

    public void deletarPorId(Long id){
        repository.deleteById(id);
    }

    @Transactional
    public void finalizarPedido(Long id){
        PedidoPresencial existente = buscarPorId(id);

        if (existente.getItens().isEmpty()){
            throw new RuntimeException("Não é possível finalizar um pedido vazio.");
        }

        existente.setStatus(StatusPedido.PEDIDO_PRONTO);
        salvar(existente);

    }

    @Transactional
    public void cancelarPedido(Long id){
        PedidoPresencial existente = buscarPorId(id);

        existente.setStatus(StatusPedido.PEDIDO_CANCELADO);
        salvar(existente);
    }
}
