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
@Table(name = "enderecos")
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Setter
    @NotBlank
    private String rua;

    @Setter
    @NotBlank
    private String numero;

    @Setter
    @NotBlank
    private String bairro;

    @Setter
    @NotBlank
    private String cidade;

    @Setter
    @NotBlank
    private String cep;

    @NotNull
    @OneToOne(mappedBy = "endereco")
    private Cliente cliente;

    public Endereco(String rua, String numero, String bairro, String cidade, String cep, Cliente cliente) {
        this.rua = rua;
        this.numero = numero;
        this.bairro = bairro;
        this.cidade = cidade;
        this.cep = cep;
        this.cliente = cliente;
    }
}
