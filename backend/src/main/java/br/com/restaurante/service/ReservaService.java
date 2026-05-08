package br.com.restaurante.service;

import br.com.restaurante.dtos.DadosCadastroReserva;
import br.com.restaurante.model.Cliente;
import br.com.restaurante.model.Mesa;
import br.com.restaurante.model.Reserva;
import br.com.restaurante.model.enums.StatusMesa;
import br.com.restaurante.model.enums.StatusReserva;
import br.com.restaurante.repository.ClienteRepository;
import br.com.restaurante.repository.MesaRepository;
import br.com.restaurante.repository.ReservaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ReservaService {

    private final ReservaRepository repository;
    private final MesaRepository mesaRepository;
    private final ClienteRepository clienteRepository;

    @Transactional
    public Reserva cadastrar(DadosCadastroReserva dto) {
        Mesa mesaReal = mesaRepository.findById(dto.mesa().getId())
                .orElseThrow(() -> new RuntimeException("Mesa não encontrada no banco de dados."));

        Cliente clienteReal = clienteRepository.findById(dto.cliente().getId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado no banco de dados."));

        if (dto.dataHora().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Não é possível agendar reservas para o passado.");
        }

        if (mesaReal.getStatus() != StatusMesa.LIVRE) {
            throw new RuntimeException("Esta mesa não está livre (Status atual: " + mesaReal.getStatus() + ")");
        }

        Reserva reserva = new Reserva(dto);
        reserva.setMesa(mesaReal);
        reserva.setCliente(clienteReal);
        reserva.setStatus(StatusReserva.CONFIRMADA);

        mesaReal.setStatus(StatusMesa.RESERVADA);
        mesaRepository.save(mesaReal);

        return repository.save(reserva);
    }

    @Transactional
    public Reserva salvar(Reserva objeto) {
        return repository.save(objeto);
    }

    @Transactional
    public List<Reserva> listarTodos() {
        return repository.findAll();
    }

    @Transactional
    public Reserva buscarPorId(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Reserva não encontrada"));
    }

    @Transactional
    public void deletarPorId(Long id) {
        repository.deleteById(id);
    }

    @Transactional
    public void cancelar(Long id){
        Reserva existente = buscarPorId(id);

        Mesa mesa = existente.getMesa();
        mesa.setStatus(StatusMesa.LIVRE);
        mesaRepository.save(mesa);

        existente.setStatus(StatusReserva.CANCELADA);
        salvar(existente);
    }

    @Transactional
    public Reserva atualizar(Long id, DadosCadastroReserva dto){
        Reserva reserva = buscarPorId(id);

        if (reserva.getStatus() != StatusReserva.CONFIRMADA) {
            throw new RuntimeException("Apenas reservas confirmadas podem ser atualizadas.");
        }
        if (dto.dataHora().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Não é possível atualizar a reserva para o passado.");
        }

        reserva.setStatus(dto.status());
        reserva.setMesa(dto.mesa());
        reserva.setCliente(dto.cliente());
        reserva.setDataHora(dto.dataHora());
        reserva.setValorReserva(dto.valorDaReserva());

        return salvar(reserva);
    }
}