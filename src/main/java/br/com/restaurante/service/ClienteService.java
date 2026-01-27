package br.com.restaurante.service;

import br.com.restaurante.dtos.DadosCadastroCliente;
import br.com.restaurante.dtos.DadosCadastroPedidoOnline;
import br.com.restaurante.dtos.DadosCadastroReserva;
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

    private final EnderecoService enderecoService;

    private final ItemCardapioService itemCardapioService;

    private final ItemCardapioRepository itemCardapioRepository;
    private final ReservaService reservaService;

    @Transactional
    public Cliente salvar(DadosCadastroCliente dto) {

        if (clienteRepository.existsByEmail(dto.email())) {
            throw new RuntimeException("E-mail já cadastrado.");
        }
        Endereco endereco = enderecoService.salvar(dto.endereco());

        Cliente cliente = new Cliente(dto);
        cliente.setEndereco(endereco);
        return clienteRepository.save(cliente);
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
    public Cliente atualizar(Long id, DadosCadastroCliente clienteAtualizado) {

        Cliente cliente = buscarPorId(id);

        Optional<Cliente> clienteComEsseEmail = clienteRepository.findByEmail(clienteAtualizado.email());
        if (clienteComEsseEmail.isPresent() &&
                !clienteComEsseEmail.get().getId().equals(id)) {
            throw new RuntimeException("O e-mail já pertence a outro cliente.");
        }

        cliente.setNome(clienteAtualizado.nome());
        cliente.setEmail(clienteAtualizado.email());
        cliente.setSenha(clienteAtualizado.senha());
        cliente.setTelefone(clienteAtualizado.telefone());


        if (clienteAtualizado.endereco() != null) {
            Endereco enderecoNovo = cliente.getEndereco();
            enderecoNovo.setRua(clienteAtualizado.endereco().rua());
            enderecoNovo.setBairro(clienteAtualizado.endereco().numero());
            enderecoNovo.setCep(clienteAtualizado.endereco().bairro());
            enderecoNovo.setCidade(clienteAtualizado.endereco().cidade());
            enderecoNovo.setNumero(clienteAtualizado.endereco().cep());
        }

        return clienteRepository.save(cliente);
    }

    @Transactional
    public void fazerReserva(Long clienteId, DadosCadastroReserva dto) {

        Cliente cliente = clienteRepository.findById(clienteId).orElseThrow(()-> new RuntimeException("Cliente não encontrado."));

        Mesa mesa = mesaRepository.findById(dto.mesaId())
                .orElseThrow(() -> new RuntimeException("Mesa não encontrada"));
        if (dto.dataHora().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Não é possível agendar reservas para o passado.");
        }

        if (mesa.getStatus().equals(StatusMesa.LIVRE)) {
            Reserva reserva = new Reserva(dto);

            reservaRepository.save(reserva);

            mesa.setStatus(StatusMesa.RESERVADA);
            mesaRepository.save(mesa);
        } else {
            throw new RuntimeException("A mesa já está Reservada ou Ocupada, tente outra mesa por favor!");
        }
    }

    @Transactional
    public void fazerPedidoOnline(DadosCadastroPedidoOnline dto) {

        Cliente cliente = buscarPorId(dto.clienteId());

        List<ItemCardapio> itens = itemCardapioRepository.findAllById(dto.itensIds());

        if (itens.isEmpty()){
            throw new RuntimeException("Pedido sem itens.");
        }

        Double total = itens.stream().mapToDouble(ItemCardapio::getPreco).sum();

        Entrega entrega = entregaRepository.save(new Entrega(cliente.getEndereco()));



        PedidoOnline pedidoOnline = new PedidoOnline();
        pedidoOnline.setCliente(cliente);
        pedidoOnline.setEntrega(entrega);
        pedidoOnline.setItens(itens);
        pedidoOnline.setValorFinal(total);
        pedidoOnline.setStatus(StatusPedido.PEDIDO_EM_PREPARO);
        pedidoOnline.setFormaDePagamento(dto.formaPagamento());
        pedidoOnline.setDataHora(LocalDateTime.now());

        pedidoOnlineRepository.save(pedidoOnline);
    }

    @Transactional
    public void cancelarReserva(Long id) {

        Reserva reserva = reservaService.buscarPorId(id);

        if (reserva.getStatus() != StatusReserva.CONFIRMADA) {
            throw new RuntimeException("A reserva não pode ser cancelada, pois ela não está ativa ou confirmada.");
        }

        reserva.setStatus(StatusReserva.CANCELADA);

        Mesa mesa = reserva.getMesa();
        mesa.setStatus(StatusMesa.LIVRE);

        reservaRepository.save(reserva);
        mesaRepository.save(mesa);
    }
}
