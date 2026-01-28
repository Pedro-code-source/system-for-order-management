package br.com.restaurante.model;

import br.com.restaurante.dtos.DadosCadastroCliente;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Entity
@Getter
@Table(name = "clientes")
public class Cliente extends Usuario {

    @Setter
    @NotBlank(message = "O nome é obrigatório")
    @Column(nullable = false)
    private String nome;

    @Setter
    @NotBlank(message = "O telefone é obrigatório")
    @Column(nullable = false)
    private String telefone;

    @Setter
    @NotNull(message = "O endereço é obrigatório")
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "endereco_id", nullable = false)
    @JsonIgnore
    private Endereco endereco;

    public Cliente(DadosCadastroCliente dados) {
        super(dados.email(), dados.senha());
        this.nome = dados.nome();
        this.telefone = dados.telefone();
    }
}