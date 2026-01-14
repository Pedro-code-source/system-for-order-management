package br.com.restaurante.model;

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
    @NotBlank
    private String nome;

    @Setter
    @NotBlank
    private String telefone;

    @Setter
    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "endereco_id")
    private Endereco endereco;

    public Cliente(String email, String senha, String nome, String telefone, Endereco endereco) {
        super(email, senha);
        this.nome = nome;
        this.telefone = telefone;
        this.endereco = endereco;
    }
}
