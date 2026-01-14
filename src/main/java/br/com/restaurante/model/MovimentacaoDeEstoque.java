package br.com.restaurante.model;

import br.com.restaurante.model.enums.TipoMovimentacao;
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
public class MovimentacaoDeEstoque {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Setter
    @NotNull
    private LocalDateTime dataCriacao;

    @Setter
    @NotNull
    private TipoMovimentacao tipoMovimentacao;

    @Setter
    @NotNull
    private Float quantidade;

    public MovimentacaoDeEstoque(LocalDateTime dataCriacao, TipoMovimentacao tipoMovimentacao, Float quantidade) {
        this.dataCriacao = dataCriacao;
        this.tipoMovimentacao = tipoMovimentacao;
        this.quantidade = quantidade;
    }
}
