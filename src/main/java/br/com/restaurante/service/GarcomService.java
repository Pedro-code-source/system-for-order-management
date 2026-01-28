package br.com.restaurante.service;

import br.com.restaurante.dtos.DadosCadastroGarcom;
import br.com.restaurante.dtos.DadosCadastroPedidoPresencial;
import br.com.restaurante.model.*;
import br.com.restaurante.model.enums.StatusMesa;
import br.com.restaurante.model.enums.StatusPedido;
import br.com.restaurante.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class GarcomService {

    private final PedidoPresencialRepository pedidoPresencialRepository;
    private final GarcomRepository garcomRepository;
    private final MesaRepository mesaRepository;
    private final ItemCardapioRepository itemCardapioRepository;

    @Transactional
    public Garcom salvar(DadosCadastroGarcom dto) {

        if (garcomRepository.existsByEmail(dto.email())) {
            throw new RuntimeException("Já existe um garçom com este e-mail.");
        }

        Garcom garcom = new Garcom();
        garcom.setSenha(dto.senha());
        garcom.setEmail(dto.email());
        garcom.setNome(dto.nome());
        return garcomRepository.save(garcom);
    }

    @Transactional
    public void deletarPorId(Long id) {
        if (garcomRepository.existsById(id)) {
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
    public PedidoPresencial registrarPedidoPresencial(DadosCadastroPedidoPresencial dto) {
        Garcom garcom = garcomRepository.findById(dto.garcom().getId())
                .orElseThrow(() -> new RuntimeException("Garçom não encontrado."));

        Mesa mesa = mesaRepository.findById(dto.mesa().getId())
                .orElseThrow(() -> new RuntimeException("Mesa não encontrada."));

        List<Long> idsItens = dto.itens().stream().map(ItemCardapio::getId).toList();
        List<ItemCardapio> itensReais = itemCardapioRepository.findAllById(idsItens);

        if (itensReais.isEmpty()) {
            throw new RuntimeException("Não é possível registrar um pedido sem itens.");
        }

        if (mesa.getStatus() != StatusMesa.LIVRE) {
            throw new RuntimeException("Esta mesa já está ocupada.");
        }

        Double valorTotal = itensReais.stream().mapToDouble(ItemCardapio::getPreco).sum();

        PedidoPresencial pedidoPresencial = new PedidoPresencial();
        pedidoPresencial.setMesa(mesa);
        pedidoPresencial.setGarcom(garcom);
        pedidoPresencial.setStatus(StatusPedido.PEDIDO_EM_PREPARO);
        pedidoPresencial.setItens(itensReais);
        pedidoPresencial.setDataHora(LocalDateTime.now());
        pedidoPresencial.setFormaDePagamento(dto.formaPagamento());
        pedidoPresencial.setValorFinal(valorTotal);

        mesa.setStatus(StatusMesa.OCUPADA);
        mesaRepository.save(mesa);

        return pedidoPresencialRepository.save(pedidoPresencial);
    }
}