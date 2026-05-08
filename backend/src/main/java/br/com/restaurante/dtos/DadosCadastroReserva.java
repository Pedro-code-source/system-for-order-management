package br.com.restaurante.dtos;

import br.com.restaurante.model.Cliente;
import br.com.restaurante.model.Mesa;
import br.com.restaurante.model.enums.StatusReserva;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record DadosCadastroReserva(
        @NotNull Mesa mesa,
        @NotNull Cliente cliente,
        @NotNull @JsonFormat(pattern = "dd/MM/yyyy HH:mm") LocalDateTime dataHora,
        @NotNull StatusReserva status,
        @NotNull Double valorDaReserva
) {}