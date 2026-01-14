package br.com.restaurante.model;

import br.com.restaurante.model.enums.StatusReserva;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Entity
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Setter
    @NotNull
    private Double valorReserva;

    @Setter
    @NotNull
    private LocalDateTime dataHora;

    @Setter
    @NotNull
    private StatusReserva status;

    public Reserva(Double valorReserva, LocalDateTime dataHora, StatusReserva status) {
        this.valorReserva = valorReserva;
        this.dataHora = dataHora;
        this.status = status;
    }
}
