package br.com.restaurante.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "entregas")
public class Entrega {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Setter
    @NotNull(message = "O endereço de entrega é obrigatório")
    @ManyToOne
    @JoinColumn(name = "endereco_id", nullable = false)
    @JsonIgnore
    private Endereco endereco;

    @OneToOne(mappedBy = "entrega")
    @JsonIgnore
    private PedidoOnline pedidoOnline;

    public Entrega(Endereco endereco) {
        this.endereco = endereco;
    }
}