package br.com.restaurante.service;

import br.com.restaurante.model.Reserva;
import br.com.restaurante.model.enums.StatusReserva;
import br.com.restaurante.repository.ReservaRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ReservaService {
    private final ReservaRepository repository;

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
