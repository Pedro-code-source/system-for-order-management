package br.com.restaurante.service;

import br.com.restaurante.model.Entrega;
import br.com.restaurante.model.PedidoOnline;
import br.com.restaurante.model.enums.StatusPedido;
import br.com.restaurante.repository.EntregaRepository;
import br.com.restaurante.repository.PedidoOnlineRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class EntregaService {

    private final EntregaRepository entregaRepository;
    private final PedidoOnlineRepository pedidoOnlineRepository;

    @Transactional
    public Entrega salvar(Entrega entrega){
        return entregaRepository.save(entrega);
    }

    @Transactional
    public void deletarPorId(Long id) {
        if (!entregaRepository.existsById(id)){
            throw new RuntimeException("Entrega não encontrada para ser deletada.");
        }
        entregaRepository.deleteById(id);
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

    @Transactional
    public Entrega atualizar(Long id, Entrega novaEntrega){
        Entrega entregaExistente = buscarPorId(id);
        entregaExistente.setEndereco(novaEntrega.getEndereco());
        return entregaRepository.save(entregaExistente);
    }

    @Transactional
    public void iniciarEntrega(Long idPedido){
        PedidoOnline pedido = pedidoOnlineRepository.findById(idPedido)
                .orElseThrow(()-> new RuntimeException("Pedido não existe."));

        pedido.setStatus(StatusPedido.PEDIDO_PRONTO);

        pedidoOnlineRepository.save(pedido);
    }

    @Transactional
    public void finalizarEntrega(Long idPedido){
        PedidoOnline pedido = pedidoOnlineRepository.findById(idPedido)
                .orElseThrow(()-> new RuntimeException("Pedido não existe."));

        pedido.setStatus(StatusPedido.PEDIDO_ENTREGUE);

        pedidoOnlineRepository.save(pedido);
    }
}