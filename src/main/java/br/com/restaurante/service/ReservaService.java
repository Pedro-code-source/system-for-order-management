package br.com.restaurante.service;

import br.com.restaurante.dtos.DadosCadastroCliente;
import br.com.restaurante.dtos.DadosCadastroReserva;
import br.com.restaurante.model.Cliente;
import br.com.restaurante.model.Endereco;
import br.com.restaurante.model.Reserva;
import br.com.restaurante.model.enums.StatusReserva;
import br.com.restaurante.repository.ReservaRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ReservaService {
    private final ReservaRepository repository;
    private final ClienteService clienteService;

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
    public void confirmar(Long id){
        Reserva existente = buscarPorId(id);

        existente.setStatus(StatusReserva.CONFIRMADA);
        salvar(existente);

    }

    @Transactional
    public void cancelar(Long id){
        Reserva existente = buscarPorId(id);

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



    @Transactional
    public boolean estaAtiva(Long id){
        Reserva existente = buscarPorId(id);

        if (existente.getStatus() == StatusReserva.CONFIRMADA){
            return true;
        }
        else {
            return false;
        }
    }
}
