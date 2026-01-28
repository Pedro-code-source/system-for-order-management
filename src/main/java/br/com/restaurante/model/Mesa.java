package br.com.restaurante.model;

import br.com.restaurante.dtos.DadosCadastroMesa;
import br.com.restaurante.model.enums.StatusMesa;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Entity
@Getter
@Table(name = "mesa")
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

    public Mesa(DadosCadastroMesa dados) {
        this.numero = dados.numero();
        this.capacidade = dados.capacidade();
        this.status = StatusMesa.LIVRE;
    }
}