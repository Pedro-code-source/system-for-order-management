package br.com.restaurante.services;

import br.com.restaurante.model.*;
import br.com.restaurante.model.enums.FormaPagamento;
import br.com.restaurante.model.enums.StatusMesa;
import br.com.restaurante.model.enums.StatusPedido;
import br.com.restaurante.model.enums.StatusReserva;
import br.com.restaurante.repository.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    private final ReservaRepository reservaRepository;
    private static final Double TAXA_FIXA = 50.0;

    private final MesaRepository mesaRepository;

    private final PedidoOnlineRepository pedidoOnlineRepository;

    private final EntregaRepository entregaRepository;

    private final EnderecoRepository enderecoRepository;

    @Transactional
    public Cliente salvar(Cliente cliente) {

        if (clienteRepository.existsByEmail(cliente.getEmail())) {
            throw new RuntimeException("E-mail já cadastrado.");
        } else {
            enderecoRepository.save(cliente.getEndereco());
            return clienteRepository.save(cliente);
        }
    }

    @Transactional
    public void deletarPorId(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new RuntimeException("Cliente não encontrado para deletar.");
        }

        try {
            clienteRepository.deleteById(id);
            clienteRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Não é possível excluir o cliente pois ele possui pedidos ou reservas vinculados.");
        }
    }

    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado com o ID: " + id));
    }

    @Transactional
    public Cliente atualizar(Long id, Cliente clienteAtualizado) {

        Optional<Cliente> clienteComEsseEmail = clienteRepository.findByEmail(clienteAtualizado.getEmail());

        if (clienteComEsseEmail.isPresent()) {
            Cliente donoDoEmail = clienteComEsseEmail.get();
            if (!donoDoEmail.getId().equals(id)) {
                throw new RuntimeException("O e-mail passado já pertence a uma outra pessoa, tente outro.");
            }
        }

        Cliente clienteExistente = buscarPorId(id);

        clienteExistente.setNome(clienteAtualizado.getNome());
        clienteExistente.setEmail(clienteAtualizado.getEmail());
        clienteExistente.setSenha(clienteAtualizado.getSenha());
        clienteExistente.setTelefone(clienteAtualizado.getTelefone());

        Endereco enderecoExistente = clienteExistente.getEndereco();
        Endereco enderecoNovo = clienteAtualizado.getEndereco();

        if (enderecoExistente != null && enderecoNovo != null) {
            enderecoExistente.setRua(enderecoNovo.getRua());
            enderecoExistente.setBairro(enderecoNovo.getBairro());
            enderecoExistente.setCep(enderecoNovo.getCep());
            enderecoExistente.setCidade(enderecoNovo.getCidade());
            enderecoExistente.setNumero(enderecoNovo.getNumero());
        }

        return clienteRepository.save(clienteExistente);
    }

    @Transactional
    public void fazerReserva(Cliente cliente, Mesa mesa, LocalDateTime dataHora) {

        if (dataHora.isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Não é possível agendar reservas para o passado.");
        }

        if (mesa.getStatus() == StatusMesa.LIVRE) {
            Reserva reserva = new Reserva(TAXA_FIXA, dataHora, StatusReserva.CONFIRMADA, cliente, mesa);

            reservaRepository.save(reserva);

            mesa.setStatus(StatusMesa.RESERVADA);
            mesaRepository.save(mesa);
        } else {
            throw new RuntimeException("A mesa já está Reservada ou Ocupada, tente outra mesa por favor!");
        }
    }

    @Transactional
    public void fazerPedidoOnline(Cliente cliente, List<ItemCardapio> itens, FormaPagamento formaPagamento) {

        if (itens.isEmpty()) {
            throw new RuntimeException("Não é possível fazer um pedido sem itens.");
        }

        Double total = itens.stream().mapToDouble(ItemCardapio::getPreco).sum();

        Entrega entrega = new Entrega(cliente.getEndereco());

        entregaRepository.save(entrega);

        PedidoOnline pedidoOnline = new PedidoOnline();
        pedidoOnline.setCliente(cliente);
        pedidoOnline.setEntrega(entrega);
        pedidoOnline.setItens(itens);
        pedidoOnline.setValorFinal(total);
        pedidoOnline.setStatus(StatusPedido.PEDIDO_EM_PREPARO);
        pedidoOnline.setFormaDePagamento(formaPagamento);
        pedidoOnline.setDataHora(LocalDateTime.now());

        pedidoOnlineRepository.save(pedidoOnline);
    }

    @Transactional
    public void cancelarReserva(Reserva reserva) {

        if (reserva.getStatus() != StatusReserva.CONFIRMADA) {
            throw new RuntimeException("A reserva não pode ser cancelada, pois ela não está ativa ou confirmada.");
        } else {
            reserva.setStatus(StatusReserva.CANCELADA);
            reserva.getMesa().setStatus(StatusMesa.LIVRE);
            reservaRepository.save(reserva);
            mesaRepository.save(reserva.getMesa());
        }

    }
}
