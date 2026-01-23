package br.com.restaurante.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Entity
@Getter
@Table(name = "garcom")
public class Garcom extends Usuario {

    @Setter
    @NotBlank(message = "O nome do garçom é obrigatório")
    @Column(nullable = false)
    private String nome;

    @OneToMany(mappedBy = "garcom")
    private List<PedidoPresencial> pedidos = new ArrayList<>();

    public Garcom(String email, String senha, String nome) {
        super(email, senha);
        this.nome = nome;
    }
}
