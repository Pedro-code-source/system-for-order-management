package br.com.restaurante.services;

import br.com.restaurante.model.Entrega;
import br.com.restaurante.model.PedidoOnline;
import br.com.restaurante.model.enums.StatusPedido;
import br.com.restaurante.repository.EntregaRepository;
import br.com.restaurante.repository.PedidoOnlineRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class EntregaService {

    private final EntregaRepository entregaRepository;

    private final PedidoOnlineRepository pedidoOnlineRepository;

    @Transactional
    public Entrega salvar(Entrega entrega){
        return entregaRepository.save(entrega);
    }
    @Transactional
    public void deletarPorId(Long id) throws Exception {

        if (!entregaRepository.existsById(id)){
            throw new Exception("Entrega não encontrada para ser deletada.");
        }
        else {
            entregaRepository.deleteById(id);
        }
    }
    @Transactional
    public List<Entrega> listarTodos(){
        return entregaRepository.findAll();
    }
    @Transactional
    public Entrega buscarPorId (Long id){
        return entregaRepository.findById(id).orElseThrow(()-> new RuntimeException("Entrega não encontrada."));
    }

    @Transactional
    public Entrega atualizar(Long id, Entrega novaEntrega){
        if (entregaRepository.existsById(id)){
            Entrega entregaExistente = buscarPorId(id);
            entregaExistente.setEndereco(novaEntrega.getEndereco());

            return entregaRepository.save(entregaExistente);
        }
        else {
            throw new RuntimeException("A entrega de ID: " + id + " não existe.");
        }
    }
    @Transactional
    public void iniciarEntrega(Long id){
        PedidoOnline pedido = pedidoOnlineRepository.findById(id).orElseThrow(()-> new RuntimeException("Pedido não existe."));

        Entrega entrega = pedido.getEntrega();
        entrega.iniciarEntrega(pedido);

        pedidoOnlineRepository.save(pedido);
    }
    @Transactional
    public void finalizarEntrega(Long id){

        PedidoOnline pedido = pedidoOnlineRepository.findById(id).orElseThrow(()-> new RuntimeException("Pedido não existe."));

        Entrega entrega = pedido.getEntrega();
        entrega.finalizarEntrega(pedido);

        pedidoOnlineRepository.save(pedido);
    }

}
