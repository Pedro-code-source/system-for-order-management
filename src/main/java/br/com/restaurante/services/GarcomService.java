package br.com.restaurante.services;

import br.com.restaurante.model.*;
import br.com.restaurante.model.enums.FormaPagamento;
import br.com.restaurante.model.enums.StatusMesa;
import br.com.restaurante.model.enums.StatusPedido;
import br.com.restaurante.repository.ClienteRepository;
import br.com.restaurante.repository.GarcomRepository;
import br.com.restaurante.repository.MesaRepository;
import br.com.restaurante.repository.PedidoPresencialRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class GarcomService {

    private final PedidoPresencialRepository pedidoPresencialRepository;
    private final ClienteRepository clienteRepository;
    private final GarcomRepository garcomRepository;
    private final MesaRepository mesaRepository;

    @Transactional
    public Garcom salvar(Garcom garcom) {
        return garcomRepository.save(garcom);
    }

    @Transactional
    public void deletarPorId(Long id) {
        Optional<Garcom> existente = garcomRepository.findById(id);
        if (!existente.isEmpty()) {
            garcomRepository.deleteById(id);
        } else {
            throw new RuntimeException("O Garcom com ID " + id + " não existe.");
        }
    }

    @Transactional
    public List<Garcom> listarTodos() {
        return garcomRepository.findAll();
    }

    @Transactional
    public Garcom buscarPorId(Long id) {
        return garcomRepository.findById(id).orElseThrow(() -> new RuntimeException("Garcom com ID " + id + " não existe."));
    }

    @Transactional
    public void atualizar(Long id, Garcom novoGarcom) {

        Garcom existente = buscarPorId(id);

        existente.setNome(novoGarcom.getNome());
        existente.setEmail(novoGarcom.getEmail());
        existente.setSenha(novoGarcom.getSenha());

        garcomRepository.save(existente);
    }

    @Transactional
    public void registrarPedidoPresencial(Long garcomId, Long clienteId, Long mesaId, List<ItemCardapio> itens, FormaPagamento formaPagamento) {

        Garcom garcom = garcomRepository.findById(garcomId).orElseThrow(() -> new RuntimeException("Garçom não encontrado."));
        Mesa mesa = mesaRepository.findById(mesaId).orElseThrow(() -> new RuntimeException("Mesa não encontrada."));
        Cliente cliente = clienteRepository.findById(clienteId).orElseThrow(() -> new RuntimeException("Cliente não encontrado."));


        if (mesa.getStatus() != StatusMesa.LIVRE) {
            throw new RuntimeException("Esta mesa já está ocupada.");
        }

        Double valorTotal = itens.stream().mapToDouble(ItemCardapio::getPreco).sum();

        PedidoPresencial pedidoPresencial = new PedidoPresencial();

        pedidoPresencial.setMesa(mesa);
        pedidoPresencial.setGarcom(garcom);
        pedidoPresencial.setStatus(StatusPedido.PEDIDO_EM_PREPARO);
        pedidoPresencial.setCliente(cliente);
        pedidoPresencial.setItens(itens);
        pedidoPresencial.setDataHora(LocalDateTime.now());
        pedidoPresencial.setFormaDePagamento(formaPagamento);
        pedidoPresencial.setValorFinal(valorTotal);

        mesa.setStatus(StatusMesa.OCUPADA);
        mesaRepository.save(mesa);

        pedidoPresencialRepository.save(pedidoPresencial);
    }

    @Transactional
    public void fecharPedido(Long pedido) {

        PedidoPresencial existente = pedidoPresencialRepository.findById(pedido).orElseThrow(()-> new RuntimeException("Pedido não encontrado."));
        existente.setStatus(StatusPedido.PEDIDO_PRONTO);
        pedidoPresencialRepository.save(existente);
    }
}
