package br.com.restaurante.model;

import br.com.restaurante.model.enums.TipoMovimentacao;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Entity
@Table (name = "movimentacao")
public class MovimentacaoDeEstoque {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Setter
    @NotNull(message = "A data de criação é obrigatória")
    @Column(nullable = false)
    private LocalDateTime dataCriacao;

    @Setter
    @NotNull(message = "O tipo de movimentação é obrigatório")
    @Column(nullable = false)
    private TipoMovimentacao tipoMovimentacao;

    @Setter
    @NotNull(message = "A quantidade movimentada é obrigatória")
    @Column(nullable = false)
    private int quantidade;

    @Setter
    @NotNull(message = "O ingrediente é obrigatório")
    @ManyToOne
    @JoinColumn(name = "ingrediente_id", nullable = false)
    private Ingrediente ingrediente;

    public MovimentacaoDeEstoque(LocalDateTime dataCriacao, TipoMovimentacao tipoMovimentacao, int quantidade, Ingrediente ingrediente) {
        this.dataCriacao = dataCriacao;
        this.tipoMovimentacao = tipoMovimentacao;
        this.quantidade = quantidade;
        this.ingrediente = ingrediente;
    }
}
