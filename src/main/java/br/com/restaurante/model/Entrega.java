package br.com.restaurante.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Entity
public class Entrega {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Setter
    @NotNull(message = "O endereço de entrega é obrigatório")
    @ManyToOne
    @JoinColumn(name = "endereco_id", nullable = false)
    private Endereco endereco;

    @OneToOne(mappedBy = "entrega")
    private PedidoOnline pedidoOnline;

    public Entrega(Endereco endereco) {
        this.endereco = endereco;
    }
}