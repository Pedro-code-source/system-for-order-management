package br.com.restaurante.model;

import br.com.restaurante.dtos.DadosEndereco;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
    @NotBlank(message = "A rua é obrigatória")
    @Column(nullable = false)
    private String rua;

    @Setter
    @NotBlank(message = "O número é obrigatório")
    @Column(nullable = false)
    private String numero;

    @Setter
    @NotBlank(message = "O bairro é obrigatório")
    @Column(nullable = false)
    private String bairro;

    @Setter
    @NotBlank(message = "A cidade é obrigatória")
    @Column(nullable = false)
    private String cidade;

    @Setter
    @NotBlank(message = "O CEP é obrigatório")
    @Column(nullable = false)
    private String cep;

    @JsonIgnore
    @OneToOne(mappedBy = "endereco")
    private Cliente cliente;

    public Endereco(DadosEndereco dados) {
        this.rua = dados.rua();
        this.numero = dados.numero();
        this.bairro = dados.bairro();
        this.cidade = dados.cidade();
        this.cep = dados.cep();
    }
}
