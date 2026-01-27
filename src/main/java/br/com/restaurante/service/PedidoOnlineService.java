package br.com.restaurante.service;

import br.com.restaurante.dtos.DadosCadastroPedidoOnline;
import br.com.restaurante.model.Endereco;
import br.com.restaurante.model.Entrega;
import br.com.restaurante.model.PedidoOnline;
import br.com.restaurante.model.enums.StatusPedido;
import br.com.restaurante.repository.EnderecoRepository;
import br.com.restaurante.repository.PedidoOnlineRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class PedidoOnlineService {
    private final PedidoOnlineRepository repository;

    private final EnderecoRepository enderecoRepository;

    @Transactional
    public PedidoOnline salvar(PedidoOnline objeto) {
        return repository.save(objeto);
    }

    @Transactional
    public List<PedidoOnline> listarTodos() {
        return repository.findAll();
    }

    @Transactional
    public PedidoOnline buscarPorId(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
    }

    @Transactional
    public void deletarPorId(Long id){
        repository.deleteById(id);
    }

    @Transactional
    public PedidoOnline atualizar(Long id, DadosCadastroPedidoOnline dto) {

        PedidoOnline pedido = buscarPorId(id);

        if (pedido.getStatus() == StatusPedido.PEDIDO_CANCELADO) {
            throw new RuntimeException("Pedidos cancelados não podem ser alterados.");
        }

        if (dto.formaPagamento() != null) {
            pedido.setFormaDePagamento(dto.formaPagamento());
        }

        if (dto.status() != null) {
            pedido.setStatus(dto.status());
        }

        return repository.save(pedido);
    }


    @Transactional
    public void finalizarPedido(Long id){
        PedidoOnline pedido = buscarPorId(id);

        if (pedido.getItens().isEmpty()){
            throw new RuntimeException("Não é possível finalizar um pedido vazio");
        }
        pedido.setStatus(StatusPedido.PEDIDO_PRONTO);
        salvar(pedido);
    }

    @Transactional
    public void cancelarPedido(Long id){
        PedidoOnline existente = buscarPorId(id);

        existente.setStatus(StatusPedido.PEDIDO_CANCELADO);
        salvar(existente);
    }

    @Transactional
    public void confirmarEndereco(Long id) {
        PedidoOnline pedido = buscarPorId(id);
        Entrega entrega = pedido.getEntrega();
        Endereco enderecoDoPerfil = entrega.getEndereco();

        Endereco enderecoSnapshot = new Endereco();

        enderecoSnapshot.setRua(enderecoDoPerfil.getRua());
        enderecoSnapshot.setBairro(enderecoDoPerfil.getBairro());
        enderecoSnapshot.setCep(enderecoDoPerfil.getCep());
        enderecoSnapshot.setCidade(enderecoDoPerfil.getCidade());
        enderecoSnapshot.setNumero(enderecoDoPerfil.getNumero());

        enderecoRepository.save(enderecoSnapshot);

        entrega.setEndereco(enderecoSnapshot);
    }

}

