package br.com.restaurante.model;

import br.com.restaurante.model.enums.StatusMesa;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Entity
@Getter
public class Mesa {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Setter
    @NotNull
    private int numero;

    @Setter
    @NotNull
    private int capacidade;

    @Setter
    @NotNull
    @Enumerated(EnumType.STRING)
    private StatusMesa status;

    public Mesa(int numero, int capacidade, StatusMesa status) {
        this.numero = numero;
        this.capacidade = capacidade;
        this.status = status;
    }
}
