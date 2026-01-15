package br.com.restaurante.model;

import br.com.restaurante.model.enums.StatusReserva;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Entity
@Table (name = "reservas")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Setter
    @NotNull(message = "O valor da reserva é obrigatório")
    @Column(nullable = false)
    private Double valorReserva;

    @Setter
    @NotNull(message = "A data e hora são obrigatórias")
    @Column(nullable = false)
    private LocalDateTime dataHora;

    @Setter
    @NotNull(message = "O status da reserva é obrigatório")
    @Column(nullable = false)
    private StatusReserva status;

    @Setter
    @NotNull(message = "O cliente é obrigatório")
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Setter
    @NotNull(message = "A mesa é obrigatória")
    @ManyToOne
    @JoinColumn(name = "mesa_id", nullable = false)
    private Mesa mesa;

    public Reserva(Double valorReserva, LocalDateTime dataHora, StatusReserva status, Cliente cliente, Mesa mesa) {
        this.valorReserva = valorReserva;
        this.dataHora = dataHora;
        this.status = status;
        this.cliente = cliente;
        this.mesa = mesa;
    }
}