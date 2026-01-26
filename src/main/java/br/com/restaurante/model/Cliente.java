package br.com.restaurante.model;

import br.com.restaurante.dtos.DadosCadastroCliente;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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
    private Endereco endereco;

    @OneToMany(mappedBy = "cliente")
    private List<Pedido> pedidos = new ArrayList<>();

    @OneToMany(mappedBy = "cliente")
    private List<Reserva> reservas = new ArrayList<>();

    public Cliente(DadosCadastroCliente dados) {
        this.nome = dados.nome();
        this.setEmail(dados.email());
        this.telefone = dados.telefone();
        this.setSenha(dados.senha());
        this.endereco = new Endereco(dados.endereco());
    }
}
