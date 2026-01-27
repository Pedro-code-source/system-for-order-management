package br.com.restaurante.dtos;

import br.com.restaurante.model.enums.StatusReserva;
import br.com.restaurante.model.Cliente;
import br.com.restaurante.model.Mesa;
import java.time.LocalDateTime;

public record DadosCadastroReserva(
        Long mesaId,
        LocalDateTime dataHora,
        StatusReserva status,
        Double valorDaReserva,
        Cliente cliente,
        Mesa mesa
) {
}
