package br.com.restaurante.service;

import br.com.restaurante.dtos.DadosCadastroGarcom;
import br.com.restaurante.dtos.DadosCadastroPedidoPresencial;
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
    public Garcom salvar(DadosCadastroGarcom dto) {
        Garcom garcom = new Garcom();
        garcom.setSenha(dto.senha());
        garcom.setEmail(dto.email());
        garcom.setNome(dto.nome());
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
    public Garcom atualizar(Long id, DadosCadastroGarcom dto) {

        Garcom existente = buscarPorId(id);

        existente.setNome(dto.nome());
        existente.setEmail(dto.email());
        existente.setSenha(dto.senha());

        return garcomRepository.save(existente);
    }

    @Transactional
    public void registrarPedidoPresencial(DadosCadastroPedidoPresencial dto) {

        Garcom garcom = garcomRepository.findById(dto.garcom().getId()).orElseThrow(() -> new RuntimeException("Garçom não encontrado."));
        Mesa mesa = mesaRepository.findById(dto.mesa().getId()).orElseThrow(() -> new RuntimeException("Mesa não encontrada."));



        if (dto.itens().isEmpty()) {
            throw new RuntimeException("Não é possível registrar um pedido sem itens.");
        }

        if (mesa.getStatus() != StatusMesa.LIVRE) {
            throw new RuntimeException("Esta mesa já está ocupada.");
        }

        Double valorTotal = dto.itens().stream().mapToDouble(ItemCardapio::getPreco).sum();

        PedidoPresencial pedidoPresencial = new PedidoPresencial();

        pedidoPresencial.setMesa(mesa);
        pedidoPresencial.setGarcom(garcom);
        pedidoPresencial.setStatus(StatusPedido.PEDIDO_EM_PREPARO);
        pedidoPresencial.setItens(dto.itens());
        pedidoPresencial.setDataHora(LocalDateTime.now());
        pedidoPresencial.setFormaDePagamento(dto.formaPagamento());
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
