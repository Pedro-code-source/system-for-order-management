package br.com.restaurante.service;

import br.com.restaurante.dtos.DadosCadastroPedidoPresencial;
import br.com.restaurante.model.PedidoPresencial;
import br.com.restaurante.model.enums.StatusMesa;
import br.com.restaurante.model.enums.StatusPedido;
import br.com.restaurante.repository.PedidoPresencialRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PedidoPresencialService {
    private final PedidoPresencialRepository repository;

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
        existente.getMesa().setStatus(StatusMesa.LIVRE);
        repository.save(existente);
    }
    @Transactional
    public PedidoPresencial atualizar(Long id, DadosCadastroPedidoPresencial dto) {
        PedidoPresencial pedido = buscarPorId(id);
        if (pedido.getStatus() == StatusPedido.PEDIDO_PRONTO) {
            throw new RuntimeException("Pedidos finalizados não podem ser alterados.");
        }
        if (dto.mesa() != null) pedido.setMesa(dto.mesa());
        if (dto.garcom() != null) pedido.setGarcom(dto.garcom());
        if (dto.formaPagamento() != null) pedido.setFormaDePagamento(dto.formaPagamento());
        return repository.save(pedido);
    }
    @Transactional
    public void cancelarPedido(Long id){
        PedidoPresencial existente = buscarPorId(id);
        existente.setStatus(StatusPedido.PEDIDO_CANCELADO);
        repository.save(existente);
    }
}