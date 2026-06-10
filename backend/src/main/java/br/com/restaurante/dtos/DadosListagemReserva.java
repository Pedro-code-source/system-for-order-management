package br.com.restaurante.dtos;

import br.com.restaurante.model.Reserva;
import br.com.restaurante.model.enums.StatusReserva;

import java.time.LocalDateTime;

public record DadosListagemReserva(
        Long id,
        Integer numeroMesa,
        LocalDateTime dataHora,
        StatusReserva status,
        Double valor,
        Long clienteId,
        String clienteNome
) {
    public DadosListagemReserva(Reserva reserva) {
        this(
                reserva.getId(),
                reserva.getMesa().getNumero(),
                reserva.getDataHora(),
                reserva.getStatus(),
                reserva.getValorReserva(),
                reserva.getCliente() != null ? reserva.getCliente().getId() : null,
                reserva.getCliente() != null ? reserva.getCliente().getNome() : null
        );
    }
}

