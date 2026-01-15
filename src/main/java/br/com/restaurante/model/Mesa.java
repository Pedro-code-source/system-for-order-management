package br.com.restaurante.model;

import br.com.restaurante.model.enums.StatusMesa;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Entity
@Getter
public class Mesa {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Setter
    @NotNull(message = "O número da mesa é obrigatório")
    @Column(nullable = false, unique = true)
    private Integer numero;

    @Setter
    @NotNull(message = "A capacidade é obrigatória")
    @Column(nullable = false)
    private Integer capacidade;

    @Setter
    @NotNull(message = "O status inicial é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusMesa status;

    @OneToMany(mappedBy = "mesa")
    private List<PedidoPresencial> pedidosPresenciais;

    @OneToMany(mappedBy = "mesa")
    private List<Reserva> reservas;

    public Mesa(Integer numero, Integer capacidade, StatusMesa status) {
        this.numero = numero;
        this.capacidade = capacidade;
        this.status = status;
    }
}
